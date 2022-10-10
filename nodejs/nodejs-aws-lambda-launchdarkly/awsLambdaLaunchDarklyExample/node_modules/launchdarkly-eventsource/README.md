# EventSource [![npm version](http://img.shields.io/npm/v/launchdarkly-eventsource.svg?style=flat-square)](http://browsenpm.org/package/launchdarkly-eventsource)[![Circle CI](https://circleci.com/gh/launchdarkly/js-eventsource/tree/master.svg?style=svg)](https://circleci.com/gh/launchdarkly/js-eventsource/tree/master)[![NPM Downloads](https://img.shields.io/npm/dm/laumchdarkly-eventsource.svg?style=flat-square)](http://npm-stat.com/charts.html?package=launchdarkly-eventsource&from=2015-09-01)[![Dependencies](https://img.shields.io/david/launchdarkly/js-eventsource.svg?style=flat-square)](https://david-dm.org/launchdarkly/js-eventsource)

This library is a pure JavaScript implementation of the [EventSource](https://html.spec.whatwg.org/multipage/server-sent-events.html#server-sent-events) client. The API aims to be W3C compatible.

You can use it with Node.js, or as a browser polyfill for
[browsers that don't have native `EventSource` support](http://caniuse.com/#feat=eventsource). However, the current implementation is inefficient in a browser due to the use of Node API shims, and is not recommended for use as a polyfill; a future release will improve this.

This is a fork of the original [EventSource](https://github.com/EventSource/eventsource) project by Aslak Hellesøy, with additions to support the requirements of the LaunchDarkly SDKs. Note that as described in the [changelog](CHANGELOG.md), the API is _not_ backward-compatible with the original package, although it can be used with minimal changes.

## Install

    npm install launchdarkly-eventsource

## Example

    npm install
    node ./example/sse-server.js
    node ./example/sse-client.js    # Node.js client
    open http://localhost:8080      # Browser client - both native and polyfill
    curl http://localhost:8080/sse  # Enjoy the simplicity of SSE

## Browser Polyfill

Just add `example/eventsource-polyfill.js` file to your web page:

```html
<script src="/eventsource-polyfill.js"></script>
```

Now you will have two global constructors:

```javascript
window.EventSourcePolyfill
window.EventSource // Unchanged if browser has defined it. Otherwise, same as window.EventSourcePolyfill
```

If you're using [webpack](https://webpack.github.io/) or [browserify](http://browserify.org/)
you can of course build your own. (The `example/eventsource-polyfill.js` is built with webpack).

## Extensions to the W3C API

All configurable extended behaviors use the optional `eventSourceInitDict` argument to the `EventSource` constructor. For instance:

```javascript
var eventSourceInitDict = { initialRetryDelayMillis: 1000, maxRetryDelayMillis: 30000 };
var es = new EventSource(url, eventSourceInitDict);
```

In a browser that is using native `EventSource` the extra argument would simply be ignored, so any code that will run in a browser, if it might or might not be using this polyfill, should assume that these options might or might be used.

### Extensions to event behavior

Like the standard `EventSource`, this implementation emits the event `open` when a stream has been started and `error` when a stream has failed for any reason. All events have a single parameter which is an instance of the `Event` class.

The `error` event has the following extended behavior: for an HTTP error response, the event object will have a `status` property (such as `401`) and optionally a `message` property (such as `"Unauthorized"`).

```javascript
es.onerror = function (err) {
  if (err) {
    if (err.status === 401 || err.status === 403) {
      console.log('not authorized');
    }
  }
};
```

The following additional events may be emitted:

* `closed`: The stream has been permanently closed, either due to a non-retryable error or because `close()` was called.
* `end`: The server ended the stream. For backward compatibility, this is not reported as an `error`, but it is still treated as one in terms of the retry logic.
* `retrying`: After an error, this indicates that `EventSource` will try to reconnect after some delay. The event object's `delayMillis` property indicates the delay in milliseconds.

### Configuring backoff and jitter

By default, `EventSource` automatically attempts to reconnect if a connection attempt fails or if an existing connection is broken. To prevent a flood of requests, there is always a delay before retrying the connection; the default value for this is 1000 milliseconds.

For backward compatibility, the default behavior is to use the same delay each time. However, it is highly recommended that you enable both exponential backoff (the delay doubles on each successive retry, up to a configurable maximum) and jitter (a random amount is subtracted from each delay), so that if a server outage causes clients to all lose their connections at the same time they will not all retry at the same time. The backoff can also be configured to reset back to the initial delay if the stream has remained active for some amount of time.

```javascript
var eventSourceInitDict = {
  initialRetryDelayMillis: 2000,   // sets initial retry delay to 2 seconds
  maxBackoffMillis: 30000,         // enables backoff, with a maximum of 30 seconds
  retryResetIntervalMillis: 60000, // backoff will reset to initial level if stream got an event at least 60 seconds before failing
  jitterRatio: 0.5                 // each delay will be reduced by a randomized jitter of up to 50%
};
```

### Configuring error retry behavior

By default, to mimic the behavior of built-in browser `EventSource` implementations, `EventSource` will retry if a connection cannot be made or if the connection is broken (using whatever retry behavior you have configured, as described above)-- but if it connects and receives an HTTP error status, it will only retry for statuses 500, 502, 503, or 504; otherwise it will just raise an `error` event and disconnect, so the application is responsible for starting a new connection.

If you set the option `errorFilter` to a function that receives an `Error` object and returns `true` or `false`, then `EventSource` will call it for any error-- either an HTTP error or an I/O error. If the function returns `true`, it will proceed with retrying the connection; if it returns `false`, it will end the connection and raise an `error` event. In this example, connections will always be retried unless there is a 401 error:

```javascript:
var eventSourceInitDict = {
  errorFilter: function(e) {
    return e.status !== 401;
  }
};
```

HTTP redirect responses (301/307) with a valid `Location` header are not considered errors, and are always immediately retried with the new URL.

### Setting HTTP request headers

You can define custom HTTP headers for the initial HTTP request. This can be useful for e.g. sending cookies or to specify an initial `Last-Event-ID` value.

HTTP headers are defined by assigning a `headers` attribute to the optional `eventSourceInitDict` argument:

```javascript
var eventSourceInitDict = { headers: { Cookie: 'test=test' } };
```

Normally, EventSource will always add the headers `Cache-Control: no-cache` (since an SSE stream should always contain live content, not cached content), and `Accept: text/event-stream`. This could cause problems if you are making a cross-origin request, since CORS has restrictions on what headers can be sent. To turn off the default headers, so that it will _only_ send the headers you specify, set the `skipDefaultHeaders` option to `true`:

```javascript
var eventSourceInitDict = {
  headers: { Cookie: 'test=test' },
  skipDefaultHeaders: true
};
```

### Setting HTTP request method/body

By default, EventSource makes a `GET` request. You can specify a different HTTP verb and/or a request body:

```javascript
var eventSourceInitDict = { method: 'POST', body: 'n=100' };
```

### Read timeout

TCP connections can sometimes fail without the client detecting an I/O error, in which case EventSource could hang forever waiting for events. Setting a `readTimeoutMillis` will cause EventSource to drop and retry the connection if that number of milliseconds ever elapses without receiving any new data from the server. If the server is known to send any "heartbeat" data at regular intervals (such as a `:` comment line, which is ignored in SSE) to indicate that the connection is still alive, set the read timeout to some number longer than that interval.

```javascript
var eventSourceInitDict = { readTimeoutMillis: 30000 };
````

### Special HTTPS configuration

In Node.js, you can customize the behavior of HTTPS requests by specifying, for instance, additional trusted CA certificates. You may use any of the special TLS options supported by Node's [`tls.connect()`](https://nodejs.org/api/tls.html#tls_tls_connect_options_callback) and [`tls.createSecureContext()`](https://nodejs.org/api/tls.html#tls_tls_createsecurecontext_options) (depending on what version of Node you are using) by putting them in an object in the `https` property of your configuration:

```javascript
var eventSourceInitDict = {
  https: {
    rejectUnauthorized: false  // causes requests to succeed even if the certificate cannot be validated
  }
};
```

This only works in Node.js, not in a browser.

### HTTP/HTTPS proxy

You can define a `proxy` option for the HTTP request to be used. This is typically useful if you are behind a corporate firewall.

```javascript
var eventSourceInitDict = { proxy: 'http://your.proxy.com' };
```

In Node.js, you can also set the `agent` option to use a tunneling agent.

### Detecting supported features

In cases where the EventSource implementation is determined at runtime (for instance, if you are in a browser that may or may not have native support for EventSource, and you are only loading this polyfill if there is no native support), you may want to  know ahead of time whether certain nonstandard features are available or not.

The special property `EventSource.supportedOptions` is an object containing a `true` value for each property name that is allowed in the constructor parameters. If `EventSource.supportedOptions.somePropertyName` is true, then you are using a version of this polyfill that supports the `somePropertyName` option. If `EventSource.supportedOptions.somePropertyName` is false or undefined, or if `EventSource.supportedOptions` does not exist, then you are using either an older version of the polyfill, or some completely different EventSource implementation.

For instance, if you want to use the `POST` method-- which built-in EventSource implementations in browsers cannot do-- but you do not know for sure whether the current EventSource is this polyfill or a built-in browser version, you should check first whether that option is supported; otherwise, if it's the browser version, it will simply ignore the method option and do a `GET` request instead which probably won't work. So your logic might look like this:

```javascript
if (EventSource.supportedOptions && EventSource.supportedOptions.method) {
  var es = new EventSource(url, { method: 'POST' });
} else {
  // do whatever you want to do if you can't do a POST request
}
```

## License

MIT-licensed. See LICENSE.
