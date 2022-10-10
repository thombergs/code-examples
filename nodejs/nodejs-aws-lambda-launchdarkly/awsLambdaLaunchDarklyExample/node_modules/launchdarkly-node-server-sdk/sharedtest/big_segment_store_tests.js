const { nullLogger } = require('../loggers');

const { withCloseable } = require('launchdarkly-js-test-helpers');

// See index.d.ts for interface documentation

const fakeUserHash = 'userhash';

function runBigSegmentStoreTests(storeFactory, clearExistingData, setMetadata, setSegments) {
  function doAllTestsWithPrefix(prefix) {
    async function withStoreAndEmptyData(action) {
      await clearExistingData(prefix);
      await withCloseable(storeFactory(prefix, nullLogger()), action);
    }

    describe('getMetadata', () => {
      it('valid value', async () => {
        const expected = { lastUpToDate: 1234567890 };
        await withStoreAndEmptyData(async store => {
          await setMetadata(prefix, expected);

          const meta = await store.getMetadata();
          expect(meta).toEqual(expected);
        });
      });

      it('no value', async () => {
        await withStoreAndEmptyData(async store => {
          const meta = await store.getMetadata();
          expect(meta).toEqual({ lastUpToDate: undefined });
        });
      });
    });

    describe('getUserMembership', () => {
      it('not found', async () => {
        await withStoreAndEmptyData(async store => {
          const membership = await store.getUserMembership(fakeUserHash);
          if (membership) {
            // either null/undefined or an empty membership would be acceptable
            expect(membership).toEqual({});
          }
        });
      });

      it('includes only', async () => {
        await withStoreAndEmptyData(async store => {
          await setSegments(prefix, fakeUserHash, ['key1', 'key2'], []);

          const membership = await store.getUserMembership(fakeUserHash);
          expect(membership).toEqual({ key1: true, key2: true });
        });
      });

      it('excludes only', async () => {
        await withStoreAndEmptyData(async store => {
          await setSegments(prefix, fakeUserHash, [], ['key1', 'key2']);

          const membership = await store.getUserMembership(fakeUserHash);
          expect(membership).toEqual({ key1: false, key2: false });
        });
      });

      it('includes and excludes', async () => {
        await withStoreAndEmptyData(async store => {
          await setSegments(prefix, fakeUserHash, ['key1', 'key2'], ['key2', 'key3']);

          const membership = await store.getUserMembership(fakeUserHash);
          expect(membership).toEqual({ key1: true, key2: true, key3: false }); // include of key2 overrides exclude
        });
      });
    });
  }

  describe('with non-empty prefix', () => {
    doAllTestsWithPrefix('testprefix');
  });

  describe('with empty prefix', () => {
    doAllTestsWithPrefix(undefined);
  });
}

module.exports = {
  runBigSegmentStoreTests,
};
