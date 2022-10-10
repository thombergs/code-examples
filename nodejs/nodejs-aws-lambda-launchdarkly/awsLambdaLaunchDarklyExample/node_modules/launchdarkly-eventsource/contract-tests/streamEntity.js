const http = require('http');
const url = require('url');

const { EventSource } = require('launchdarkly-eventsource');

function StreamEntity(options) {
  const s = {};
  const listeningForType = {};
  const tag = options.tag;
  let closed = false;
  let callbackCounter = 0;

  function log(s) {
    console.log(`[${tag}] INFO: ${s}`);
  }

  function logError(tag, s) {
    console.log(`[${tag}] ERROR: ${s}`);
  }

  function sendMessage(message) {
    if (closed) {
      return;
    }
    callbackCounter++;
    const callbackUrl = options.callbackUrl + '/' + callbackCounter;
    const reqParams = {
      ...url.parse(callbackUrl),
      method: 'POST',
      headers: { 'Content-Type': 'application/json '}
    };
    const req = http.request(reqParams, res => {
      if (!closed && res.statusCode >= 300) {
        logError(`Callback to ${callbackUrl} returned HTTP error ${res.statusCode}`);
      }
    });
    req.on('error', e => {
      if (!closed) {
        logError(`Callback to ${callbackUrl} failed: ${e}`);
      }
    });
    req.write(JSON.stringify(message));
    req.end();
  }

  log(`Starting stream from ${options.streamUrl}`);

  const eventSourceParams = {};
  if (options.headers) {
    eventSourceParams.headers = options.headers;
  }
  if (options.method) {
    eventSourceParams.method = options.method;
    eventSourceParams.body = options.body;
  }
  if (options.readTimeoutMs) {
    eventSourceParams.readTimeoutMillis = options.readTimeoutMs;
  }
  if (options.initialDelayMs) {
    eventSourceParams.initialRetryDelayMillis = options.initialDelayMs;
  }

  s.onMessage = event => {
    log(`Received message from stream (${event.type})`);
    sendMessage({
      kind: 'event',
      event: {
        type: event.type,
        data: event.data,
        id: event.lastEventId
      }
    });
  };

  s.close = () => {
    s.closed = true;
    s.sse.close();
    log('Test ended');
  };

  s.doCommand = params => {
    switch (params.command) {
      case 'listen':
        const eventType = params.listen.type;
        if (!listeningForType[eventType]) {
          listeningForType[eventType] = true;
          s.sse.addEventListener(eventType, s.onMessage);
        }
        return true;

      default:
        return false;
    }
  }

  s.sse = new EventSource(options.streamUrl, eventSourceParams);

  s.sse.onopen = () => {
    log('Opened stream');
  };
  s.sse.onclosed = () => {
    log('Closed stream');
  };
  s.sse.onmessage = s.onMessage;
  s.sse.onerror = error => {
    log(`Received error from stream: ${error}`);
    sendMessage({
      kind: 'error',
      error: error.toString()
    });
  };

  return s;
}

module.exports.StreamEntity = StreamEntity;
