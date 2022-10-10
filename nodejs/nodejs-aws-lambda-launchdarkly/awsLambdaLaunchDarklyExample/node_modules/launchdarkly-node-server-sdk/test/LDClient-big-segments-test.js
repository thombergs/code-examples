const { hashForUserKey } = require('../big_segments');
const { makeBigSegmentRef } = require('../evaluator');
const { TestData } = require('../integrations');
const stubs = require('./stubs');
const { makeSegmentMatchClause } = require('./evaluator_helpers');
const { withCloseable } = require('launchdarkly-js-test-helpers');

describe('LDClient - big segments', () => {

  const user = { key: 'userkey' };
  const bigSegment = {
    key: 'segmentkey',
    version: 1,
    unbounded: true,
    generation: 2,
  };
  const flag = {
    key: 'flagkey',
    on: true,
    variations: [ false, true ],
    fallthrough: { variation: 0 },
    rules: [
      { variation: 1, clauses: [ makeSegmentMatchClause(bigSegment) ] },
    ],
  }

  async function makeClient(bigSegmentsStore, config) {
    const td = TestData();
    td.usePreconfiguredFlag(flag);
    td.usePreconfiguredSegment(bigSegment);

    const bigSegmentsConfig = {
      store: bigSegmentsStore && (() => bigSegmentsStore),
      ...(config && config.bigSegments),
    };

    return stubs.createClient({ ...config, updateProcessor: td, bigSegments: bigSegmentsConfig });
  }

  it('user not found in big segment store', async () => {
    const store = {
      getMetadata: async () => { return { lastUpToDate: new Date().getTime() } },
      getUserMembership: async userHash => null,
    };

    await withCloseable(await makeClient(store), async client => {
      await client.waitForInitialization();
      const result = await client.variationDetail(flag.key, user, false);
      expect(result.value).toBe(false);
      expect(result.reason.bigSegmentsStatus).toEqual('HEALTHY');
    });
  });

  it('user found, segment matched', async () => {
    const membership = { [makeBigSegmentRef(bigSegment)]: true };
    const store = {
      getMetadata: async () => { return { lastUpToDate: new Date().getTime() } },
      getUserMembership: async userHash => (userHash === hashForUserKey(user.key) ? membership : null),
    };

    await withCloseable(await makeClient(store), async client => {
      await client.waitForInitialization();
      const result = await client.variationDetail(flag.key, user, false);
      expect(result.value).toBe(true);
      expect(result.reason.bigSegmentsStatus).toEqual('HEALTHY');
    });
  });

  it('store error', async () => {
    const store = {
      getMetadata: async () => { return { lastUpToDate: new Date().getTime() } },
      getUserMembership: async userHash => { throw new Error("sorry") },
    };

    await withCloseable(await makeClient(store), async client => {
      await client.waitForInitialization();
      const result = await client.variationDetail(flag.key, user, false);
      expect(result.value).toBe(false);
      expect(result.reason.bigSegmentsStatus).toEqual('STORE_ERROR');
    });
  });

  it('not configured', async () => {
    await withCloseable(await makeClient(null), async client => {
      await client.waitForInitialization();
      const result = await client.variationDetail(flag.key, user, false);
      expect(result.value).toBe(false);
      expect(result.reason.bigSegmentsStatus).toEqual('NOT_CONFIGURED');
    });
  });

});
