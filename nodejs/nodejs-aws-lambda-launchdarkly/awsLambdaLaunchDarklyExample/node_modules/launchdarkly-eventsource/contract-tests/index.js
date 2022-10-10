const http = require('http');
const url = require('url');

const express = require('express');
const bodyParser = require('body-parser');

const { StreamEntity } = require('./streamEntity');

const app = express();

const port = 8000;

let streamCounter = 0;
const streams = {};

app.use(bodyParser.json());

app.get('/', (req, res) => {
  res.header("Content-Type", "application/json");
  res.json({
    'capabilities': [
      'event-type-listeners',
      'headers',
      'post',
      'read-timeout',
      'report',
    ]
  })
});

app.delete('/', (req, res) => {
  console.log('Test service has told us to exit');
  res.status(204);
  res.send();
  process.exit();
});

app.post('/', (req, res) => {
  const options = req.body;
  const tag = options.tag;

  if (!options.streamUrl || !options.callbackUrl) {
    log(tag, `Received request with incomplete parameters: ${JSON.stringify(options)}`);
    res.status(400);
    res.send();
    return;
  }

  streamCounter += 1;
  const streamId = streamCounter.toString()
  const streamResourceUrl = `/streams/${streamId}`;

  const stream = StreamEntity(options);
  streams[streamId] = stream;

  res.status(201);
  res.set('Location', streamResourceUrl);
  res.send();
});

app.post('/streams/:id', function(req, res) {
  const stream = streams[req.params.id];
  if (!stream) {
    res.status(404);
  } else if (!stream.doCommand(req.body)) {
    res.status(400);
  } else {
    res.status(204);
  }
  res.send();
});

app.delete('/streams/:id', function(req, res) {
  const stream = streams[req.params.id];
  if (!stream) {
    res.status(404);
    res.send();
    return;
  }
  stream.close();
  delete streams[req.params.id];
  res.status(204);
  res.send();
});

var server = app.listen(port, function () {
  console.log('Listening on port %d', port);
});
