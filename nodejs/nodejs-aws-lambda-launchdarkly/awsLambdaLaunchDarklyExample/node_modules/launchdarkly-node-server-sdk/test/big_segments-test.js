const { BigSegmentStoreManager, hashForUserKey } = require('../big_segments');
const { nullLogger } = require('../loggers');
const { AsyncQueue } = require('launchdarkly-js-test-helpers');

describe('BigSegmentStoreManager', () => {
  const userKey = 'userkey', userHash = hashForUserKey(userKey);
  const logger = nullLogger();
  const alwaysUpToDate = async () => {
    return { lastUpToDate: new Date().getTime() };
  };
  const alwaysStale = async () => {
    return { lastUpToDate: new Date().getTime() - 1000000 };
  };
  function membershipForExpectedUser(expectedMembership) {
    return async (hash) => {
      expect(hash).toEqual(userHash);
      return expectedMembership;
    }
  }
  async function withManager(store, config, action) {
    const m = BigSegmentStoreManager(store, config, logger);
    try {
      await action(m);
    } finally {
      m.close();
    }
  }

  describe('membership query', () => {
    it('with uncached result and healthy status', async () => {
      const expectedMembership = { key1: true, key2: true };
      const store = {
        getMetadata: alwaysUpToDate,
        getUserMembership: membershipForExpectedUser(expectedMembership),
      };
      await withManager(store, {}, async m => {
        const result = await m.getUserMembership(userKey);
        expect(result).toEqual([ expectedMembership, 'HEALTHY' ]);
      });
    });

    it('with cached result and healthy status', async () => {
      const expectedMembership = { key1: true, key2: true };
      let queryCount = 0;
      const store = {
        getMetadata: alwaysUpToDate,
        getUserMembership: async hash => {
          queryCount++;
          return await membershipForExpectedUser(expectedMembership)(hash);
        },
      };
      await withManager(store, {}, async m => {
        const result1 = await m.getUserMembership(userKey);
        expect(result1).toEqual([ expectedMembership, 'HEALTHY' ]);
        const result2 = await m.getUserMembership(userKey);
        expect(result2).toEqual(result1);

        expect(queryCount).toEqual(1);
      });
    });

    it('with stale status', async () => {
      const expectedMembership = { key1: true, key2: true };
      const store = {
        getMetadata: alwaysStale,
        getUserMembership: membershipForExpectedUser(expectedMembership),
      };
      await withManager(store, {}, async m => {
        const result = await m.getUserMembership(userKey);
        expect(result).toEqual([ expectedMembership, 'STALE' ]);
      });
    });

    it('with stale status due to no store metadata', async () => {
      const expectedMembership = { key1: true, key2: true };
      const store = {
        getMetadata: async () => undefined,
        getUserMembership: membershipForExpectedUser(expectedMembership),
      };
      await withManager(store, {}, async m => {
        const result = await m.getUserMembership(userKey);
        expect(result).toEqual([ expectedMembership, 'STALE' ]);
      });
    });

    it('least recent user is evicted from cache', async () => {
      const userKey1 = 'userkey1', userKey2 = 'userkey2', userKey3 = 'userkey3';
      const userHash1 = hashForUserKey(userKey1), userHash2 = hashForUserKey(userKey2), userHash3 = hashForUserKey(userKey3);
      const memberships = {};
      memberships[userHash1] = { seg1: true };
      memberships[userHash2] = { seg2: true };
      memberships[userHash3] = { seg3: true };
      let queriedUsers = [];
      const store = {
        getMetadata: alwaysUpToDate,
        getUserMembership: async hash => {
          queriedUsers.push(hash);
          return memberships[hash];
        },
      };
      const config = { userCacheSize: 2 };
      await withManager(store, config, async m => {
        const result1 = await m.getUserMembership(userKey1);
        const result2 = await m.getUserMembership(userKey2);
        const result3 = await m.getUserMembership(userKey3);
        expect(result1).toEqual([ memberships[userHash1], 'HEALTHY' ]);
        expect(result2).toEqual([ memberships[userHash2], 'HEALTHY' ]);
        expect(result3).toEqual([ memberships[userHash3], 'HEALTHY' ]);

        expect(queriedUsers).toEqual([ userHash1, userHash2, userHash3 ]);

        // Since the capacity is only 2 and userKey1 was the least recently used, that key should be
        // evicted by the userKey3 query. Now only userKey2 and userKey3 are in the cache, and
        // querying them again should not cause a new query to the store.

        const result2a = await m.getUserMembership(userKey2);
        const result3a = await m.getUserMembership(userKey3);
        expect(result2a).toEqual(result2);
        expect(result3a).toEqual(result3);

        expect(queriedUsers).toEqual([ userHash1, userHash2, userHash3 ]);
        
        const result1a = await m.getUserMembership(userKey1);
        expect(result1a).toEqual(result1);

        expect(queriedUsers).toEqual([ userHash1, userHash2, userHash3, userHash1 ]);
      });
    });
  });

  describe('status polling', () => {
    it('detects store unavailability', async () => {
      const store = {
        getMetadata: alwaysUpToDate,
      };
      await withManager(store, { statusPollInterval: 0.01 }, async m => {
        const status1 = await m.statusProvider.requireStatus();
        expect(status1.available).toBe(true);

        const statuses = new AsyncQueue();
        m.statusProvider.on('change', s => statuses.add(s));

        store.getMetadata = async () => { throw new Error('sorry'); };

        const status2 = await statuses.take();
        expect(status2.available).toBe(false);
        expect(m.statusProvider.getStatus()).toEqual(status2);

        store.getMetadata = alwaysUpToDate;

        const status3 = await statuses.take();
        expect(status3.available).toBe(true);
        expect(m.statusProvider.getStatus()).toEqual(status3);
      });
    });

    it('detects stale status', async () => {
      const store = {
        getMetadata: alwaysUpToDate,
      };
      await withManager(store, { statusPollInterval: 0.01, staleAfter: 0.2 }, async m => {
        const status1 = await m.statusProvider.requireStatus();
        expect(status1.stale).toBe(false);

        const statuses = new AsyncQueue();
        m.statusProvider.on('change', s => statuses.add(s));

        store.getMetadata = alwaysStale;

        const status2 = await statuses.take();
        expect(status2.stale).toBe(true);
        expect(m.statusProvider.getStatus()).toEqual(status2);

        store.getMetadata = alwaysUpToDate;

        const status3 = await statuses.take();
        expect(status3.stale).toBe(false);
        expect(m.statusProvider.getStatus()).toEqual(status3);
      });
    });
  });
});
