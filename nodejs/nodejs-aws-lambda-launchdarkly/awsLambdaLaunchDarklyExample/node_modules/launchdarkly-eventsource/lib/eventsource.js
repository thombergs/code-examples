var retryDelay = require('./retry-delay')

var parse = require('url').parse
var URL = require('url').URL
var events = require('events')
var https = require('https')
var http = require('http')
var util = require('util')

var httpsOptions = [
  'pfx', 'key', 'passphrase', 'cert', 'ca', 'ciphers',
  'rejectUnauthorized', 'secureProtocol', 'servername', 'checkServerIdentity'
]

var bom = [239, 187, 191]
var colon = 58
var space = 32
var lineFeed = 10
var carriageReturn = 13

function hasBom (buf) {
  return bom.every(function (charCode, index) {
    return buf[index] === charCode
  })
}

/**
 * Creates a new EventSource object
 *
 * @param {String} url the URL to which to connect
 * @param {Object} [eventSourceInitDict] extra init params. See README for details.
 * @api public
 **/
function EventSource (url, eventSourceInitDict) {
  var readyState = EventSource.CONNECTING
  var config = eventSourceInitDict || {}

  Object.defineProperty(this, 'readyState', {
    get: function () {
      return readyState
    }
  })

  Object.defineProperty(this, 'url', {
    get: function () {
      return url
    }
  })

  var self = this
  self.reconnectInterval = 1000

  var req
  var lastEventId = ''
  if (config.headers && config.headers['Last-Event-ID']) {
    lastEventId = config.headers['Last-Event-ID']
  }

  var discardTrailingNewline = false
  var data, eventName, eventId

  var reconnectUrl = null
  var retryDelayStrategy = new retryDelay.RetryDelayStrategy(
    config.initialRetryDelayMillis !== null && config.initialRetryDelayMillis !== undefined ? config.initialRetryDelayMillis : 1000,
    config.retryResetIntervalMillis,
    config.maxBackoffMillis ? retryDelay.defaultBackoff(config.maxBackoffMillis) : null,
    config.jitterRatio ? retryDelay.defaultJitter(config.jitterRatio) : null
  )

  var streamOriginUrl = new URL(url).origin

  function makeRequestUrlAndOptions () {
    // Returns { url, options }; url is null/undefined if the URL properties are in options
    var actualUrl = url
    var options = { headers: {} }
    if (!config.skipDefaultHeaders) {
      options.headers['Cache-Control'] = 'no-cache'
      options.headers['Accept'] = 'text/event-stream'
    }
    if (lastEventId) options.headers['Last-Event-ID'] = lastEventId
    if (config.headers) {
      for (var key in config.headers) {
        if (config.headers.hasOwnProperty(key)) {
          options.headers[key] = config.headers[key]
        }
      }
    }

    // Legacy: this should be specified as `eventSourceInitDict.https.rejectUnauthorized`,
    // but for now exists as a backwards-compatibility layer
    options.rejectUnauthorized = !!config.rejectUnauthorized

    // If specify http proxy, make the request to sent to the proxy server,
    // and include the original url in path and Host headers
    if (config.proxy) {
      actualUrl = null
      var parsedUrl = parse(url)
      var proxy = parse(config.proxy)
      options.protocol = proxy.protocol === 'https:' ? 'https:' : 'http:'
      options.path = url
      options.headers.Host = parsedUrl.host
      options.hostname = proxy.hostname
      options.host = proxy.host
      options.port = proxy.port
      if (proxy.username) {
        options.auth = proxy.username + ':' + proxy.password
      }
    }

    // When running in Node, proxies can also be specified as an agent
    if (config.agent) {
      options.agent = config.agent
    }

    // If https options are specified, merge them into the request options
    if (config.https) {
      for (var optName in config.https) {
        if (httpsOptions.indexOf(optName) === -1) {
          continue
        }

        var option = config.https[optName]
        if (option !== undefined) {
          options[optName] = option
        }
      }
    }

    // Pass this on to the XHR
    if (config.withCredentials !== undefined) {
      options.withCredentials = config.withCredentials
    }

    if (config.method) {
      options.method = config.method
    }

    return { url: actualUrl, options: options }
  }

  function defaultErrorFilter (error) {
    if (error.status) {
      var s = error.status
      return s === 500 || s === 502 || s === 503 || s === 504
    }
    return true // always return I/O errors
  }

  function failed (error) {
    if (readyState === EventSource.CLOSED) {
      return
    }
    var errorEvent = error ? new Event('error', error) : new Event('end')
    var shouldRetry = (config.errorFilter || defaultErrorFilter)(errorEvent)
    if (shouldRetry) {
      readyState = EventSource.CONNECTING
      _emit(errorEvent)
      scheduleReconnect()
    } else {
      _emit(errorEvent)
      readyState = EventSource.CLOSED
      _emit(new Event('closed'))
    }
  }

  function scheduleReconnect () {
    if (readyState !== EventSource.CONNECTING) return
    var delay = retryDelayStrategy.nextRetryDelay(new Date().getTime())

    // The url may have been changed by a temporary redirect. If that's the case, revert it now.
    if (reconnectUrl) {
      url = reconnectUrl
      reconnectUrl = null
    }

    var event = new Event('retrying')
    event.delayMillis = delay
    _emit(event)

    setTimeout(function () {
      if (readyState !== EventSource.CONNECTING) return
      connect()
    }, delay)
  }

  function connect () {
    var urlAndOptions = makeRequestUrlAndOptions()
    var isSecure = urlAndOptions.options.protocol === 'https:' ||
      (urlAndOptions.url && urlAndOptions.url.startsWith('https:'))

    var callback = function (res) {
      // Handle HTTP redirects
      if (res.statusCode === 301 || res.statusCode === 307) {
        if (!res.headers.location) {
          // Server sent redirect response without Location header.
          failed({ status: res.statusCode, message: res.statusMessage })
          return
        }
        if (res.statusCode === 307) reconnectUrl = url
        url = res.headers.location
        process.nextTick(connect) // don't go through the scheduleReconnect logic since this isn't an error
        return
      }

      // Handle HTTP errors
      if (res.statusCode !== 200) {
        failed({ status: res.statusCode, message: res.statusMessage })
        return
      }

      data = ''
      eventName = ''
      eventId = undefined
      
      readyState = EventSource.OPEN
      res.on('close', function () {
        res.removeAllListeners('close')
        res.removeAllListeners('end')
        failed()
      })

      res.on('end', function () {
        res.removeAllListeners('close')
        res.removeAllListeners('end')
        failed()
      })
      _emit(new Event('open'))

      // text/event-stream parser adapted from webkit's
      // Source/WebCore/page/EventSource.cpp
      var isFirst = true
      var buf
      var startingPos = 0
      var startingFieldLength = -1

      res.on('data', function (chunk) {
        buf = buf ? Buffer.concat([buf, chunk]) : chunk
        if (isFirst && hasBom(buf)) {
          buf = buf.slice(bom.length)
        }

        isFirst = false
        var pos = 0
        var length = buf.length

        while (pos < length) {
          if (discardTrailingNewline) {
            if (buf[pos] === lineFeed) {
              ++pos
            }
            discardTrailingNewline = false
          }

          var lineLength = -1
          var fieldLength = startingFieldLength
          var c

          for (var i = startingPos; lineLength < 0 && i < length; ++i) {
            c = buf[i]
            if (c === colon) {
              if (fieldLength < 0) {
                fieldLength = i - pos
              }
            } else if (c === carriageReturn) {
              discardTrailingNewline = true
              lineLength = i - pos
            } else if (c === lineFeed) {
              lineLength = i - pos
            }
          }

          if (lineLength < 0) {
            startingPos = length - pos
            startingFieldLength = fieldLength
            break
          } else {
            startingPos = 0
            startingFieldLength = -1
          }

          parseEventStreamLine(buf, pos, fieldLength, lineLength)

          pos += lineLength + 1
        }

        if (pos === length) {
          buf = void 0
        } else if (pos > 0) {
          buf = buf.slice(pos)
        }
      })
    }
    var api = isSecure ? https : http
    req = urlAndOptions.url ?
      api.request(urlAndOptions.url, urlAndOptions.options, callback) :
      api.request(urlAndOptions.options, callback)

    if (config.readTimeoutMillis) {
      req.setTimeout(config.readTimeoutMillis)
    }

    if (config.body) {
      req.write(config.body)
    }

    req.on('error', function (err) {
      failed({ message: err.message })
    })

    req.on('timeout', function () {
      failed({ message: 'Read timeout, received no data in ' + config.readTimeoutMillis +
          'ms, assuming connection is dead' })
    })

    if (req.setNoDelay) req.setNoDelay(true)
    req.end()
  }

  connect()

  function _emit (event) {
    if (event) {
      self.emit(event.type, event)
    }
  }

  this._close = function () {
    if (readyState === EventSource.CLOSED) return
    readyState = EventSource.CLOSED
    if (req.abort) req.abort()
    if (req.xhr && req.xhr.abort) req.xhr.abort()
    _emit(new Event('closed'))
  }

  function receivedEvent (event) {
    retryDelayStrategy.setGoodSince(new Date().getTime())
    _emit(event)
  }

  function parseEventStreamLine (buf, pos, fieldLength, lineLength) {
    if (lineLength === 0) {
      if (data.length > 0) {
        var type = eventName || 'message'
        if (eventId !== undefined) {
          lastEventId = eventId
        }
        var event = new MessageEvent(type, {
          data: data.slice(0, -1), // remove trailing newline
          lastEventId: lastEventId,
          origin: streamOriginUrl
        })
        data = ''
        eventId = undefined
        receivedEvent(event)
      }
      eventName = void 0
    } else {
      var noValue = fieldLength < 0
      var step = 0
      var field = buf.slice(pos, pos + (noValue ? lineLength : fieldLength)).toString()

      if (noValue) {
        step = lineLength
      } else if (buf[pos + fieldLength + 1] !== space) {
        step = fieldLength + 1
      } else {
        step = fieldLength + 2
      }
      pos += step

      var valueLength = lineLength - step
      var value = buf.slice(pos, pos + valueLength).toString()

      if (field === 'data') {
        data += value + '\n'
      } else if (field === 'event') {
        eventName = value
      } else if (field === 'id') {
        if (!value.includes("\u0000")) {
          eventId = value
        }
      } else if (field === 'retry') {
        var retry = parseInt(value, 10)
        if (!Number.isNaN(retry)) {
          self.reconnectInterval = retry
          retryDelayStrategy.setBaseDelay(retry)
        }
      }
    }
  }
}

module.exports = {
  EventSource: EventSource
}

util.inherits(EventSource, events.EventEmitter)
EventSource.prototype.constructor = EventSource; // make stacktraces readable

['open', 'end', 'error', 'message', 'retrying', 'closed'].forEach(function (method) {
  Object.defineProperty(EventSource.prototype, 'on' + method, {
    /**
     * Returns the current listener
     *
     * @return {Mixed} the set function or undefined
     * @api private
     */
    get: function get () {
      var listener = this.listeners(method)[0]
      return listener ? (listener._listener ? listener._listener : listener) : undefined
    },

    /**
     * Start listening for events
     *
     * @param {Function} listener the listener
     * @return {Mixed} the set function or undefined
     * @api private
     */
    set: function set (listener) {
      this.removeAllListeners(method)
      this.addEventListener(method, listener)
    }
  })
})

/**
 * Ready states
 */
Object.defineProperty(EventSource, 'CONNECTING', {enumerable: true, value: 0})
Object.defineProperty(EventSource, 'OPEN', {enumerable: true, value: 1})
Object.defineProperty(EventSource, 'CLOSED', {enumerable: true, value: 2})

EventSource.prototype.CONNECTING = 0
EventSource.prototype.OPEN = 1
EventSource.prototype.CLOSED = 2

/**
 * Adds the EventSource.supportedOptions property that allows application code to know which
 * custom options are supported by this polyfill.
 */
var supportedOptions = [
  'errorFilter',
  'headers',
  'https',
  'initialRetryDelayMillis',
  'jitterRatio',
  'maxBackoffMillis',
  'method',
  'proxy',
  'retryResetIntervalMillis',
  'skipDefaultHeaders',
  'withCredentials'
]
var supportedOptionsObject = {}
for (var i in supportedOptions) {
  Object.defineProperty(supportedOptionsObject, supportedOptions[i], {enumerable: true, value: true})
  // Using custom properties for this allows us to make them read-only.
}
Object.defineProperty(EventSource, 'supportedOptions', {enumerable: true, value: supportedOptionsObject})

/**
 * Closes the connection, if one is made, and sets the readyState attribute to 2 (closed)
 *
 * @see https://developer.mozilla.org/en-US/docs/Web/API/EventSource/close
 * @api public
 */
EventSource.prototype.close = function () {
  this._close()
}

/**
 * Emulates the W3C Browser based WebSocket interface using addEventListener.
 *
 * @param {String} type A string representing the event type to listen out for
 * @param {Function} listener callback
 * @see https://developer.mozilla.org/en/DOM/element.addEventListener
 * @see http://dev.w3.org/html5/websockets/#the-websocket-interface
 * @api public
 */
EventSource.prototype.addEventListener = function addEventListener (type, listener) {
  if (typeof listener === 'function') {
    // store a reference so we can return the original function again
    listener._listener = listener
    this.on(type, listener)
  }
}

/**
 * Emulates the W3C Browser based WebSocket interface using dispatchEvent.
 *
 * @param {Event} event An event to be dispatched
 * @see https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/dispatchEvent
 * @api public
 */
EventSource.prototype.dispatchEvent = function dispatchEvent (event) {
  if (!event.type) {
    throw new Error('UNSPECIFIED_EVENT_TYPE_ERR')
  }
  // if event is instance of an CustomEvent (or has 'details' property),
  // send the detail object as the payload for the event
  this.emit(event.type, event.detail)
}

/**
 * Emulates the W3C Browser based WebSocket interface using removeEventListener.
 *
 * @param {String} type A string representing the event type to remove
 * @param {Function} listener callback
 * @see https://developer.mozilla.org/en/DOM/element.removeEventListener
 * @see http://dev.w3.org/html5/websockets/#the-websocket-interface
 * @api public
 */
EventSource.prototype.removeEventListener = function removeEventListener (type, listener) {
  if (typeof listener === 'function') {
    listener._listener = undefined
    this.removeListener(type, listener)
  }
}

/**
 * W3C Event
 *
 * @see http://www.w3.org/TR/DOM-Level-3-Events/#interface-Event
 * @api private
 */
function Event (type, optionalProperties) {
  Object.defineProperty(this, 'type', { writable: false, value: type, enumerable: true })
  if (optionalProperties) {
    for (var f in optionalProperties) {
      if (optionalProperties.hasOwnProperty(f)) {
        Object.defineProperty(this, f, { writable: false, value: optionalProperties[f], enumerable: true })
      }
    }
  }
}

/**
 * W3C MessageEvent
 *
 * @see http://www.w3.org/TR/webmessaging/#event-definitions
 * @api private
 */
function MessageEvent (type, eventInitDict) {
  Object.defineProperty(this, 'type', { writable: false, value: type, enumerable: true })
  for (var f in eventInitDict) {
    if (eventInitDict.hasOwnProperty(f)) {
      Object.defineProperty(this, f, { writable: false, value: eventInitDict[f], enumerable: true })
    }
  }
}
