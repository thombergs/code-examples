const express = require('express');
const bodyParser = require('body-parser');

const { Log } = require('./log');
const { newSdkClientEntity, badCommandError } = require('./sdkClientEntity');

const app = express();
let server = null;

const port = 8000;

let clientCounter = 0;
const clients = {};

const mainLog = Log('service');

app.use(bodyParser.json());

app.get('/', (req, res) => {
  res.header('Content-Type', 'application/json');
  res.json({
    capabilities: [
      'server-side',
      'all-flags-client-side-only',
      'all-flags-details-only-for-tracked-flags',
      'all-flags-with-reasons',
      'tags',
    ],
  });
});

app.delete('/', (req, res) => {
  mainLog.info('Test service has told us to exit');
  res.status(204);
  res.send();

  // Defer the following actions till after the response has been sent
  setTimeout(() => {
    server.close(() => process.exit());
    // We force-quit with process.exit because, even after closing the server, there could be some
    // scheduled tasks lingering if an SDK instance didn't get cleaned up properly, and we don't want
    // that to prevent us from quitting.
  }, 1);
});

app.post('/', async (req, res) => {
  const options = req.body;

  clientCounter += 1;
  const clientId = clientCounter.toString();
  const resourceUrl = `/clients/${clientId}`;

  try {
    const client = await newSdkClientEntity(options);
    clients[clientId] = client;

    res.status(201);
    res.set('Location', resourceUrl);
  } catch (e) {
    res.status(500);
    const message = e.message || JSON.stringify(e);
    mainLog.error('Error creating client: ' + message);
    res.write(message);
  }
  res.send();
});

app.post('/clients/:id', async (req, res) => {
  const client = clients[req.params.id];
  if (!client) {
    res.status(404);
  } else {
    try {
      const respValue = await client.doCommand(req.body);
      if (respValue) {
        res.status(200);
        res.write(JSON.stringify(respValue));
      } else {
        res.status(204);
      }
    } catch (e) {
      const isBadRequest = e === badCommandError;
      res.status(isBadRequest ? 400 : 500);
      res.write(e.message || JSON.stringify(e));
      if (!isBadRequest && e.stack) {
        console.log(e.stack);
      }
    }
  }
  res.send();
});

app.delete('/clients/:id', async (req, res) => {
  const client = clients[req.params.id];
  if (!client) {
    res.status(404);
    res.send();
  } else {
    client.close();
    delete clients[req.params.id];
    res.status(204);
    res.send();
  }
});

server = app.listen(port, () => {
  console.log('Listening on port %d', port);
});
