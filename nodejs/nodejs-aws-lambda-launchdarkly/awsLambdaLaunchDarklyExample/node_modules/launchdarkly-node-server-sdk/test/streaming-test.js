const { DiagnosticId, DiagnosticsManager } = require('../diagnostic_events');
const InMemoryFeatureStore = require('../feature_store');
const StreamProcessor = require('../streaming');
import * as httpUtils from '../utils/httpUtils';
const dataKind = require('../versioned_data_kind');

const { failOnResolve, failOnTimeout, promisifySingle } = require('launchdarkly-js-test-helpers');
const stubs = require('./stubs');

describe('StreamProcessor', () => {
  const sdkKey = 'SDK_KEY';

  function fakeEventSource() {
    var es = { handlers: {} };
    es.constructor = function(url, options) {
      es.url = url;
      es.options = options;
      this.addEventListener = (type, handler) => {
        es.handlers[type] = handler;
      };
      this.close = () => {
        es.closed = true;
      };
      this.simulateError = e => {
        const shouldRetry = es.options.errorFilter(e);
        if (!shouldRetry) {
          es.closed = true;
        }
      }
      es.instance = this;
    };
    return es;
  }

  function createProcessor(config, es, diagnosticsManager) {
    return StreamProcessor(sdkKey, config, null, diagnosticsManager, es.constructor);
  }

  function expectJsonError(err, config) {
    expect(err).not.toBe(undefined);
    expect(err.message).toEqual('Malformed JSON data in event stream');
    expect(config.logger.error).toHaveBeenCalled();
  }

  it('uses expected URL', function() {
    var config = { streamUri: 'http://test' };
    var es = fakeEventSource();
    var sp = createProcessor(config, es);
    sp.start();
    expect(es.url).toEqual(config.streamUri + '/all');
  });

  it('sets expected headers', function() {
    var config = { streamUri: 'http://test' };
    var es = fakeEventSource();
    var sp = createProcessor(config, es);
    sp.start();
    expect(es.options.headers).toMatchObject(httpUtils.getDefaultHeaders(sdkKey, config));
  });

  describe('put message', function() {
    var putData = {
      data: {
        flags: {
          flagkey: { key: 'flagkey', version: 1 }
        },
        segments: {
          segkey: { key: 'segkey', version: 2 }
        }
      }
    };

    it('causes flags and segments to be stored', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);
      sp.start();

      es.handlers.put({ data: JSON.stringify(putData) });

      var flag = await promisifySingle(featureStore.initialized)();
      expect(flag).toEqual(true);
      
      var f = await promisifySingle(featureStore.get)(dataKind.features, 'flagkey');
      expect(f.version).toEqual(1);
      var s = await promisifySingle(featureStore.get)(dataKind.segments, 'segkey');
      expect(s.version).toEqual(2);
    });

    it('calls initialization callback', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);
      
      var waitUntilStarted = promisifySingle(sp.start)();
      es.handlers.put({ data: JSON.stringify(putData) });
      var result = await waitUntilStarted;
      expect(result).toBe(undefined);
    });

    it('passes error to callback if data is invalid', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);
      
      var waitUntilStarted = promisifySingle(sp.start)();
      es.handlers.put({ data: '{not-good' });
      var result = await waitUntilStarted;
      expectJsonError(result, config);
    });

    it('updates diagnostic stats', async () => {
      const featureStore = InMemoryFeatureStore();
      const config = { featureStore: featureStore, logger: stubs.stubLogger() };

      const id = DiagnosticId('sdk-key');
      const manager = DiagnosticsManager(config, id, 100000);
      const startTime = new Date().getTime();

      const es = fakeEventSource();
      const sp = createProcessor(config, es, manager);

      const waitUntilStarted = promisifySingle(sp.start)();
      es.handlers.put({ data: JSON.stringify(putData) });
      await waitUntilStarted;

      const event = manager.createStatsEventAndReset(0, 0, 0);
      expect(event.streamInits.length).toEqual(1);
      const si = event.streamInits[0];
      expect(si.timestamp).toBeGreaterThanOrEqual(startTime);
      expect(si.failed).not.toBeTruthy();
      expect(si.durationMillis).toBeGreaterThanOrEqual(0);
    });
  });

  describe('patch message', function() {
    it('updates flag', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);

      var patchData = {
        path: '/flags/flagkey',
        data: { key: 'flagkey', version: 1 }
      };

      sp.start();
      es.handlers.patch({ data: JSON.stringify(patchData) });

      var f = await promisifySingle(featureStore.get)(dataKind.features, 'flagkey');
      expect(f.version).toEqual(1);
    });

    it('updates segment', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);

      var patchData = {
        path: '/segments/segkey',
        data: { key: 'segkey', version: 1 }
      };

      sp.start();
      es.handlers.patch({ data: JSON.stringify(patchData) });

      var s = await promisifySingle(featureStore.get)(dataKind.segments, 'segkey');
      expect(s.version).toEqual(1);
    });

    it('passes error to callback if data is invalid', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);
      
      var waitForCallback = promisifySingle(sp.start)();
      es.handlers.patch({ data: '{not-good' });
      var result = await waitForCallback;
      expectJsonError(result, config);
    });
  });

  describe('delete message', function() {
    it('deletes flag', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);

      sp.start();

      var flag = { key: 'flagkey', version: 1 }
      await promisifySingle(featureStore.upsert)(dataKind.features, flag);
      var f = await promisifySingle(featureStore.get)(dataKind.features, flag.key);
      expect(f).toEqual(flag);

      var deleteData = { path: '/flags/' + flag.key, version: 2 };
      es.handlers.delete({ data: JSON.stringify(deleteData) });

      var f = await promisifySingle(featureStore.get)(dataKind.features, flag.key);
      expect(f).toBe(null);
    });

    it('deletes segment', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);

      sp.start();

      var segment = { key: 'segkey', version: 1 }
      await promisifySingle(featureStore.upsert)(dataKind.segments, segment);
      var s = await promisifySingle(featureStore.get)(dataKind.segments, segment.key);
      expect(s).toEqual(segment);

      var deleteData = { path: '/segments/' + segment.key, version: 2 };
      es.handlers.delete({ data: JSON.stringify(deleteData) });

      s = await promisifySingle(featureStore.get)(dataKind.segments, segment.key);
      expect(s).toBe(null);
    });

    it('passes error to callback if data is invalid', async () => {
      var featureStore = InMemoryFeatureStore();
      var config = { featureStore: featureStore, logger: stubs.stubLogger() };
      var es = fakeEventSource();
      var sp = createProcessor(config, es);
      
      var waitForResult = promisifySingle(sp.start)();
      es.handlers.delete({ data: '{not-good' });
      var result = await waitForResult;
      expectJsonError(result, config);
    });
  });

  async function testRecoverableError(err) {
    const logCapture = stubs.asyncLogCapture();
    const featureStore = InMemoryFeatureStore();
    const config = { featureStore: featureStore, logger: logCapture.logger };
    const id = DiagnosticId('sdk-key');
    const manager = DiagnosticsManager(config, id, 100000);
    const startTime = new Date().getTime();

    const es = fakeEventSource();
    const sp = createProcessor(config, es, manager);

    // Note that the callback for start() will *not* be called; it only reports failure and provides the
    // error object if it's an unrecovable error.
    const waitForStart = promisifySingle(sp.start)(); 

    es.instance.simulateError(err);

    await failOnTimeout(logCapture.warn.take(), 1000, 'timed out waiting for log message');

    await failOnResolve(waitForStart, 50, 'initialization completed unexpectedly');
    await failOnResolve(logCapture.error.take(), 50, 'got unexpected log error');

    expect(es.closed).not.toBeTruthy();

    const event = manager.createStatsEventAndReset(0, 0, 0);
    expect(event.streamInits.length).toEqual(1);
    const si = event.streamInits[0];
    expect(si.timestamp).toBeGreaterThanOrEqual(startTime);
    expect(si.failed).toBeTruthy();
    expect(si.durationMillis).toBeGreaterThanOrEqual(0);
  }
  
  it.each([400, 408, 429, 500, 503])(
    'continues retrying after error %d',
    async (status) => {
      const err = new Error('sorry');
      err.status = status;
      await testRecoverableError(err);
    }
  );

  it('continues retrying after I/O error', async () => await testRecoverableError(new Error('sorry')));

  async function testUnrecoverableError(err) {
    const logCapture = stubs.asyncLogCapture();
    const featureStore = InMemoryFeatureStore();
    const config = { featureStore: featureStore, logger: logCapture.logger };
    const id = DiagnosticId('sdk-key');
    const manager = DiagnosticsManager(config, id, 100000);
    const startTime = new Date().getTime();

    const es = fakeEventSource();
    const sp = createProcessor(config, es, manager);

    const waitForStart = promisifySingle(sp.start)();  
    es.instance.simulateError(err);
    const errReceived = await failOnTimeout(waitForStart, 1000, 'timed out waiting for error result');

    expect(errReceived).toEqual(err);

    await failOnTimeout(logCapture.error.take(), 50, 'timed out waiting for log error');

    expect(es.closed).toBe(true);

    const event = manager.createStatsEventAndReset(0, 0, 0);
    expect(event.streamInits.length).toEqual(1);
    const si = event.streamInits[0];
    expect(si.timestamp).toBeGreaterThanOrEqual(startTime);
    expect(si.failed).toBeTruthy();
    expect(si.durationMillis).toBeGreaterThanOrEqual(0);
  }
  
  it.each([401, 403])(
    'stops retrying after error %d',
    async (status) => {
      const err = new Error('sorry');
      err.status = status;
      await testUnrecoverableError(err);
    }
  );
});
