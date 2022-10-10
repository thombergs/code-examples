const InMemoryFeatureStore = require('../feature_store');
const PollingProcessor = require('../polling');
const dataKind = require('../versioned_data_kind');
const { AsyncQueue, failOnResolve, failOnTimeout, promisify, promisifySingle } = require('launchdarkly-js-test-helpers');
const stubs = require('./stubs');

describe('PollingProcessor', () => {
  const longInterval = 100000;
  const allData = { flags: { flag: { version: 1 } }, segments: { segment: { version: 1 } } };
  const jsonData = JSON.stringify(allData);

  let store;
  let config;
  let processor;

  beforeEach(() => {
    store = InMemoryFeatureStore();
    config = { featureStore: store, pollInterval: longInterval, logger: stubs.stubLogger() };
  });

  afterEach(() => {
    processor && processor.stop();
  });

  it('makes no request before start', () => {
    const requestor = {
      requestAllData: jest.fn()
    };
    processor = PollingProcessor(config, requestor);

    expect(requestor.requestAllData).not.toHaveBeenCalled();
  });

  it('polls immediately on start', () => {
    const requestor = {
      requestAllData: jest.fn()
    };
    processor = PollingProcessor(config, requestor);

    processor.start(() => {});

    expect(requestor.requestAllData).toHaveBeenCalledTimes(1);
  });

  it('calls callback on success', async () => {
    const requestor = {
      requestAllData: cb => cb(null, jsonData)
    };
    processor = PollingProcessor(config, requestor);

    const err = await new Promise(resolve => processor.start(resolve));
    expect(err).not.toBe(expect.anything());
  });

  it('initializes feature store', async () => {
    const requestor = {
      requestAllData: cb => cb(null, jsonData)
    };
    processor = PollingProcessor(config, requestor);

    await promisify(processor.start)();

    const flags = await promisifySingle(store.all)(dataKind.features);
    expect(flags).toEqual(allData.flags);
    const segments = await promisifySingle(store.all)(dataKind.segments);
    expect(segments).toEqual(allData.segments);
  });

  it('polls repeatedly', async() => {
    const calls = new AsyncQueue();
    const requestor = {
      requestAllData: cb => {
        calls.add();
        cb(null, jsonData);
      }
    };
    config.pollInterval = 0.05;  // note, pollInterval is in seconds
    processor = PollingProcessor(config, requestor);

    processor.start(() => {});
    const startTime = new Date().getTime();
    for (let i = 0; i < 4; i++) {
      await failOnTimeout(calls.take(), 500, 'timed out waiting for poll request #' + (i + 1));
    }
    expect(new Date().getTime() - startTime).toBeLessThanOrEqual(500);
  });

  async function testRecoverableError(err) {
    const calls = new AsyncQueue();
    let count = 0;
    const requestor = {
      // The first two calls will return the error; the third will succeed.
      requestAllData: cb => {
        calls.add();
        count++;
        if (count > 2) {
          cb(null, jsonData);
        } else {
          cb(err);
        }
      }
    };
    config.pollInterval = 0.05;
    processor = PollingProcessor(config, requestor);

    let errReceived;
    processor.start(e => { errReceived = e; });

    for (let i = 0; i < 3; i++) {
      await failOnTimeout(calls.take(), 500, 'timed out waiting for poll request #' + (i + 1));
    }

    expect(config.logger.error).not.toHaveBeenCalled();
    expect(errReceived).toBeUndefined();
  }
  
  it.each([400, 408, 429, 500, 503])(
    'continues polling after error %d',
    async (status) => {
      const err = new Error('sorry');
      err.status = status;
      await testRecoverableError(err);
    }
  );

  it('continues polling after I/O error', async () => await testRecoverableError(new Error('sorry')));

  async function testUnrecoverableError(status) {
    const err = new Error('sorry');
    err.status = status;

    const calls = new AsyncQueue();
    const requestor = {
      requestAllData: cb => {
        calls.add();
        cb(err);
      }
    };
    config.pollInterval = 0.05;
    processor = PollingProcessor(config, requestor);

    const result = new AsyncQueue();
    processor.start(e => result.add(e));

    const errReceived = await failOnTimeout(result.take(), 1000, 'timed out waiting for initialization to complete');
    expect(errReceived.message).toMatch(new RegExp('error ' + status + '.*giving up permanently'));

    await failOnTimeout(calls.take(), 10, 'expected initial poll request but did not see one');
    await failOnResolve(calls.take(), 100, 'received unexpected second poll request');
    expect(config.logger.error).toHaveBeenCalledTimes(1);
  }
  
  it.each([401, 403])(
    'stops polling after error %d',
    testUnrecoverableError
  );  
});
