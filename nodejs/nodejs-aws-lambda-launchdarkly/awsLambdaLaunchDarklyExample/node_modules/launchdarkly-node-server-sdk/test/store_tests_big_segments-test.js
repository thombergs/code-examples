const { runBigSegmentStoreTests } = require('../sharedtest/store_tests');

// This verifies that the runBigSegmentStoreTests test suite behaves as expected as long as the
// BigSegmentStore implementation behaves as expected, so we can distinguish between flaws in the
// implementations and flaws in the test logic. Unfortunately, we can't verify that the test suite
// would *fail* in cases where they ought to fail (if the store does not behave as expected),
// because in Jest there is no way to say to express a conditional expectation of failure. But
// at least we can verify that they won't fail for no good reason.

const mockDatabase = {};

function createStore(prefix, logger) {
  if (!mockDatabase[prefix]) {
    mockDatabase[prefix] = {};
  }
  return {
    getMetadata: async () => {
      return mockDatabase[prefix]['$metadata'] || { lastUpToDate: undefined };
    },
    getUserMembership: async userHashKey => {
      return mockDatabase[prefix][userHashKey];
    },
    close: () => {},
  }
}

async function clearExistingData(prefix) {
  mockDatabase[prefix] = {};
}

async function setMetadata(prefix, metadata) {
  if (!mockDatabase[prefix]) {
    mockDatabase[prefix] = {};
  }
  mockDatabase[prefix]['$metadata'] = metadata;
}

async function setSegments(prefix, userHashKey, included, excluded) {
  if (!mockDatabase[prefix]) {
    mockDatabase[prefix] = {};
  }
  const membership = {};
  for (const ref of (excluded || [])) {
    membership[ref] = false;
  }
  for (const ref of (included || [])) {
    membership[ref] = true;
  }
  mockDatabase[prefix][userHashKey] = membership;
}

describe('runBigSegmentStoreTests', () => {
  describe('tests pass with valid mock store', () => {
    runBigSegmentStoreTests(
      createStore,
      clearExistingData,
      setMetadata,
      setSegments
    );
  });
});
