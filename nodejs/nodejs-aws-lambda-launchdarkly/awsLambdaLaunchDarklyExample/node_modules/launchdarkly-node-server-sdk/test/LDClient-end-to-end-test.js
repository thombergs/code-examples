const LDClient = require('../index.js');
import { AsyncQueue, TestHttpHandlers, TestHttpServer, withCloseable } from 'launchdarkly-js-test-helpers';
import { stubLogger } from './stubs';

async function withAllServers(asyncCallback) {
  return await withCloseable(TestHttpServer.start, async pollingServer =>
    withCloseable(TestHttpServer.start, async streamingServer =>
      withCloseable(TestHttpServer.start, async eventsServer => {
        const servers = { polling: pollingServer, streaming: streamingServer, events: eventsServer };
        const baseConfig = {
          baseUri: pollingServer.url,
          streamUri: streamingServer.url,
          eventsUri: eventsServer.url,
          logger: stubLogger(),
        };
        return await asyncCallback(servers, baseConfig);
      })
    )
  );
}

describe('LDClient end-to-end', () => {
  const sdkKey = 'sdkKey';
  const flagKey = 'flagKey';
  const expectedFlagValue = 'yes';
  const flag = {
    key: flagKey,
    version: 1,
    on: false,
    offVariation: 0,
    variations: [ expectedFlagValue, 'no' ]
  };
  const allData = { flags: { flagKey: flag }, segments: {} };

  const user = { key: 'userKey' };

  it('starts in polling mode', async () => {
    await withAllServers(async (servers, config) => {
      servers.polling.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respondJson(allData));
      servers.events.forMethodAndPath('post', '/bulk', TestHttpHandlers.respond(200));
      servers.events.forMethodAndPath('post', '/diagnostic', TestHttpHandlers.respond(200));

      config.stream = false;
      await withCloseable(LDClient.init(sdkKey, config), async client => {
        await client.waitForInitialization();
        expect(client.initialized()).toBe(true);

        const value = await client.variation(flag.key, user);
        expect(value).toEqual(expectedFlagValue);

        await client.flush();
      });

      expect(servers.polling.requestCount()).toEqual(1);
      expect(servers.streaming.requestCount()).toEqual(0);
      expect(servers.events.requestCount()).toEqual(2);
      const req0 = await servers.events.nextRequest();
      expect(req0.path).toEqual('/diagnostic');
      const req1 = await servers.events.nextRequest();
      expect(req1.path).toEqual('/bulk');
    });
  });
  
  it('fails in polling mode with 401 error', async () => {
    await withAllServers(async (servers, config) => {
      servers.polling.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respond(401));
      servers.events.forMethodAndPath('post', '/bulk', TestHttpHandlers.respond(200));
      servers.events.forMethodAndPath('post', '/diagnostic', TestHttpHandlers.respond(200));

      config.stream = false;

      await withCloseable(LDClient.init(sdkKey, config), async client => {
        await expect(client.waitForInitialization()).rejects.toThrow();
        expect(client.initialized()).toBe(false);
      });

      expect(servers.polling.requestCount()).toEqual(1);
      expect(servers.streaming.requestCount()).toEqual(0);
    });
  });

  it('starts in streaming mode', async () => {
    await withAllServers(async (servers, config) => {
      const streamEvent = { type: 'put', data: JSON.stringify({ data: allData }) };
      await withCloseable(new AsyncQueue(), async events => {
        events.add(streamEvent);
        servers.streaming.forMethodAndPath('get', '/all', TestHttpHandlers.sseStream(events));
        servers.events.forMethodAndPath('post', '/bulk', TestHttpHandlers.respond(200));
        servers.events.forMethodAndPath('post', '/diagnostic', TestHttpHandlers.respond(200));

        await withCloseable(LDClient.init(sdkKey, config), async client => {
          await client.waitForInitialization();
          expect(client.initialized()).toBe(true);

          const value = await client.variation(flag.key, user);
          expect(value).toEqual(expectedFlagValue);

          await client.flush();  
        });

        expect(servers.polling.requestCount()).toEqual(0);
        expect(servers.streaming.requestCount()).toEqual(1);
        expect(servers.events.requestCount()).toEqual(2);
        const req0 = await servers.events.nextRequest();
        expect(req0.path).toEqual('/diagnostic');
        const req1 = await servers.events.nextRequest();
        expect(req1.path).toEqual('/bulk');
      });
    });
  });

  it('fails in streaming mode with 401 error', async () => {
    await withAllServers(async (servers, config) => {
      servers.streaming.forMethodAndPath('get', '/all', TestHttpHandlers.respond(401));
      servers.events.forMethodAndPath('post', '/bulk', TestHttpHandlers.respond(200));
      servers.events.forMethodAndPath('post', '/diagnostic', TestHttpHandlers.respond(200));

      await withCloseable(LDClient.init(sdkKey, config), async client => {
        await expect(client.waitForInitialization()).rejects.toThrow();
        expect(client.initialized()).toBe(false);
      });

      expect(servers.polling.requestCount()).toEqual(0);
      expect(servers.streaming.requestCount()).toEqual(1);
    });
  });
  
  it('can use proxy in polling mode', async () => {
    await withCloseable(TestHttpServer.startProxy, async fakeProxyServer => {
      await withCloseable(TestHttpServer.start, async pollingServer => {
        pollingServer.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respondJson(allData));

        const config = {
          baseUri: pollingServer.url,
          proxyHost: fakeProxyServer.hostname,
          proxyPort: fakeProxyServer.port,
          stream: false,
          sendEvents: false,
          logger: stubLogger(),
        };

        await withCloseable(LDClient.init(sdkKey, config), async client => {
          await client.waitForInitialization();
          expect(client.initialized()).toBe(true);

          // If the proxy server did not log a request then the SDK did not actually use the proxy
          expect(fakeProxyServer.requestCount()).toEqual(1);
          const req = await fakeProxyServer.nextRequest();
          expect(req.path).toEqual(pollingServer.url);
        });
      });
    });
  });

  it('can use proxy in streaming mode', async () => {
    await withCloseable(TestHttpServer.startProxy, async fakeProxyServer => {
      await withCloseable(TestHttpServer.start, async streamingServer => {
        const streamEvent = { type: 'put', data: JSON.stringify({ data: allData }) };
        await withCloseable(new AsyncQueue(), async events => {
          events.add(streamEvent);
          streamingServer.forMethodAndPath('get', '/all', TestHttpHandlers.sseStream(events));
  
          const config = {
            streamUri: streamingServer.url,
            proxyHost: fakeProxyServer.hostname,
            proxyPort: fakeProxyServer.port,
            sendEvents: false,
            logger: stubLogger(),
          };

          await withCloseable(LDClient.init(sdkKey, config), async client => {
            await client.waitForInitialization();
            expect(client.initialized()).toBe(true);

            // If the proxy server did not log a request then the SDK did not actually use the proxy
            expect(fakeProxyServer.requestCount()).toEqual(1);
            const req = await fakeProxyServer.nextRequest();
            expect(req.path).toEqual(streamingServer.url);
          });
        });
      });
    });
  });
  
  it('can use proxy for events', async () => {
    await withCloseable(TestHttpServer.startProxy, async fakeProxyServer => {
      await withAllServers(async (servers) => {
        servers.polling.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respondJson(allData));
        servers.events.forMethodAndPath('post', '/diagnostic', TestHttpHandlers.respond(200));

        const config = {
          baseUri: servers.polling.url,
          eventsUri: servers.events.url,
          proxyHost: fakeProxyServer.hostname,
          proxyPort: fakeProxyServer.port,
          stream: false,
          logger: stubLogger(),
        };

        await withCloseable(LDClient.init(sdkKey, config), async client => {
          await client.waitForInitialization();
          expect(client.initialized()).toBe(true);

          // If the proxy server did not log a request then the SDK did not actually use the proxy
          expect(fakeProxyServer.requestCount()).toEqual(2);
          const req0 = await fakeProxyServer.nextRequest();
          const req1 = await fakeProxyServer.nextRequest();
          if (req0.path === servers.polling.url) {
            expect(req1.path).toEqual(servers.events.url);
          } else {
            expect(req0.path).toEqual(servers.events.url);
            expect(req1.path).toEqual(servers.polling.url);
          }
        });
      });
    });
  });
});
