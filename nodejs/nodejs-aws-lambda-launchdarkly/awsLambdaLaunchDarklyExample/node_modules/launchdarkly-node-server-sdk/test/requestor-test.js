import Requestor from '../requestor';
import * as httpUtils from '../utils/httpUtils';
import * as dataKind from '../versioned_data_kind';

import { promisify, TestHttpHandlers, TestHttpServer, withCloseable } from 'launchdarkly-js-test-helpers';

describe('Requestor', () => {
  const sdkKey = 'x';
  const badUri = 'http://bad-uri';
  const someData = { key: { version: 1 } };
  const allData = { flags: someData, segments: someData };

  describe('requestAllData', () => {
    it('gets data', async () =>
      await withCloseable(TestHttpServer.start, async server => {
        server.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respondJson(allData));
        const r = Requestor(sdkKey, { baseUri: server.url });
        const result = await promisify(r.requestAllData)();
        expect(JSON.parse(result)).toEqual(allData);
      })
    );

    it('returns error result for HTTP error', async () =>
      await withCloseable(TestHttpServer.start, async server => {
        server.forMethodAndPath('get', '/sdk/latest-all', TestHttpHandlers.respond(401));
        const r = Requestor(sdkKey, { baseUri: server.url });
        const req = promisify(r.requestAllData)();
        await expect(req).rejects.toThrow(/401/);
      })
    );

    it('returns error result for network error', async () => {
      const r = Requestor(sdkKey, { baseUri: badUri });
      const req = promisify(r.requestAllData)();
      await expect(req).rejects.toThrow(/bad-uri/);
    });

    it('stores and sends etag', async () => {
      const etag = "abc123";
      await withCloseable(TestHttpServer.start, async server => {
        server.forMethodAndPath('get', '/sdk/latest-all', (req, res) => {
          if (req.headers['if-none-match'] === etag) {
            TestHttpHandlers.respond(304)(req, res);
          } else {
            TestHttpHandlers.respond(200, { 'content-type': 'application/json', etag }, JSON.stringify(allData))(req, res);
          }
        });
        const r = Requestor(sdkKey, { baseUri: server.url });
        const result1 = await promisify(r.requestAllData)();
        expect(JSON.parse(result1)).toEqual(allData);
        const result2 = await promisify(r.requestAllData)();
        expect(result2).toEqual(null);
        const req1 = await server.nextRequest();
        const req2 = await server.nextRequest();
        expect(req1.headers['if-none-match']).toBe(undefined);
        expect(req2.headers['if-none-match']).toEqual(etag);
      })
    });
  });
});
