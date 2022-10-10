import * as LDClient from '../index';

import {
  AsyncQueue,
  TestHttpHandlers,
  TestHttpServer,
  failOnTimeout,
  withCloseable
} from 'launchdarkly-js-test-helpers';
import * as stubs from './stubs';

describe('LDClient TLS configuration', () => {
  const sdkKey = 'secret';
  let logger = stubs.stubLogger();

  it('can connect via HTTPS to a server with a self-signed certificate, if CA is specified', async () => {
    await withCloseable(TestHttpServer.startSecure, async server => {
      server.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respondJson({}));

      const config = {
        baseUri: server.url,
        sendEvents: false,
        stream: false,
        logger: stubs.stubLogger(),
        tlsParams: { ca: server.certificate },
        diagnosticOptOut: true,
      };
      
      await withCloseable(LDClient.init(sdkKey, config), async client => {
        await client.waitForInitialization();
      });
    });
  });

  it('cannot connect via HTTPS to a server with a self-signed certificate, using default config', async () => {
    await withCloseable(TestHttpServer.startSecure, async server => {
      server.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respondJson({}));

      const logCapture = stubs.asyncLogCapture();
      const config = {
        baseUri: server.url,
        sendEvents: false,
        stream: false,
        logger: logCapture.logger,
        diagnosticOptOut: true,
      };

      await withCloseable(LDClient.init(sdkKey, config), async client => {
        const message1 = await failOnTimeout(logCapture.warn.take(), 1000, 'timed out waiting for log message');
        expect(message1).toMatch(/only disable the streaming API/); // irrelevant message due to our use of polling mode
        const message2 = await failOnTimeout(logCapture.warn.take(), 1000, 'timed out waiting for log message');
        expect(message2).toMatch(/self.signed/);
      });
    });
  });

  it('can use custom TLS options for streaming as well as polling', async () => {
    await withCloseable(TestHttpServer.startSecure, async server => {
      const eventData = { data: { flags: { flag: { version: 1 } }, segments: {} } };
      await withCloseable(new AsyncQueue(), async events => {
        events.add({ type: 'put', data: JSON.stringify(eventData) });
        server.forMethodAndPath('get', '/stream/all', TestHttpHandlers.sseStream(events));

        const config = {
          baseUri: server.url,
          streamUri: server.url + '/stream',
          sendEvents: false,
          logger: logger,
          tlsParams: { ca: server.certificate },
          diagnosticOptOut: true,
        };

        await withCloseable(LDClient.init(sdkKey, config), async client => {
          await client.waitForInitialization(); // this won't return until the stream receives the "put" event
        });
      });
    });
  });

  it('can use custom TLS options for posting events', async () => {
    await withCloseable(TestHttpServer.startSecure, async server => {
      server.forMethodAndPath('post', '/events/bulk', TestHttpHandlers.respond(200));
      server.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respondJson({}));

      const config = {
        baseUri: server.url,
        eventsUri: server.url + '/events',
        stream: false,
        logger: stubs.stubLogger(),
        tlsParams: { ca: server.certificate },
        diagnosticOptOut: true,
      };

      await withCloseable(LDClient.init(sdkKey, config), async client => {
        await client.waitForInitialization();
        client.identify({ key: 'user' });
        await client.flush();

        const flagsRequest = await server.nextRequest();
        expect(flagsRequest.path).toEqual('/sdk/latest-all');
        
        const eventsRequest = await server.nextRequest();
        expect(eventsRequest.path).toEqual('/events/bulk');
        const eventData = JSON.parse(eventsRequest.body);
        expect(eventData.length).toEqual(1);
        expect(eventData[0].kind).toEqual('identify');
      });
    });
  });
});
