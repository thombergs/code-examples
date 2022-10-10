const { TestData } = require('../integrations');
const stubs = require('./stubs');

describe('LDClient.allFlagsState', () => {
  const defaultUser = { key: 'user' };

  it('captures flag state', async () => {
    const value1 = 'value1', value2 = 'value2', value3 = 'value3';
    const flag1 = {
      key: 'key1',
      version: 100,
      on: false,
      offVariation: 0,
      variations: [ value1 ]
    };
    const flag2 = {
      key: 'key2',
      version: 200,
      on: false,
      offVariation: 1,
      variations: [ 'x', value2 ],
      trackEvents: true,
      debugEventsUntilDate: 1000
    };
    // flag3 has an experiment (evaluation is a fallthrough and TrackEventsFallthrough is on)
    const flag3 = {
      key: 'key3',
      version: 300,
      on: true,
      fallthrough: { variation: 1 },
      variations: [ 'x', value3 ],
      trackEvents: false,
      trackEventsFallthrough: true
    };
    const td = TestData();
    td.usePreconfiguredFlag(flag1);
    td.usePreconfiguredFlag(flag2);
    td.usePreconfiguredFlag(flag3);

    const client = stubs.createClient({ updateProcessor: td });
    await client.waitForInitialization();
    const state = await client.allFlagsState(defaultUser);
    expect(state.valid).toEqual(true);
    expect(state.allValues()).toEqual({ [flag1.key]: value1, [flag2.key]: value2, [flag3.key]: value3 });
    expect(state.getFlagValue(flag1.key)).toEqual(value1);
    expect(state.toJSON()).toEqual({
      [flag1.key]: value1,
      [flag2.key]: value2,
      [flag3.key]: value3,
      $flagsState: {
        [flag1.key]: {
          version: flag1.version,
          variation: 0,
        },
        [flag2.key]: {
          version: flag2.version,
          variation: 1,
          trackEvents: true,
          debugEventsUntilDate: 1000
        },
        [flag3.key]: {
          version: flag3.version,
          variation: 1,
          reason: { kind: 'FALLTHROUGH' },
          trackEvents: true,
          trackReason: true
        }
      },
      $valid: true
    });
  });

  it('can filter for only client-side flags', async () => {
    const td = TestData();
    td.usePreconfiguredFlag({ key: 'server-side-1', on: false, offVariation: 0, variations: ['a'], clientSide: false });
    td.usePreconfiguredFlag({ key: 'server-side-2', on: false, offVariation: 0, variations: ['b'], clientSide: false });
    td.usePreconfiguredFlag({ key: 'client-side-1', on: false, offVariation: 0, variations: ['value1'], clientSide: true });
    td.usePreconfiguredFlag({ key: 'client-side-2', on: false, offVariation: 0, variations: ['value2'], clientSide: true });
    const client = stubs.createClient({ updateProcessor: td });
    await client.waitForInitialization();
    const state = await client.allFlagsState(defaultUser, { clientSideOnly: true });
    expect(state.valid).toEqual(true);
    expect(state.allValues()).toEqual({ 'client-side-1': 'value1', 'client-side-2': 'value2' });
  });

  it('can include reasons', async () => {
    const td = TestData();
    td.usePreconfiguredFlag({
      key: 'feature',
      version: 100,
      offVariation: 1,
      variations: ['a', 'b'],
      trackEvents: true,
      debugEventsUntilDate: 1000
    });
    const client = stubs.createClient({ updateProcessor: td });
    await client.waitForInitialization();
    const state = await client.allFlagsState(defaultUser, { withReasons: true });
    expect(state.valid).toEqual(true);
    expect(state.allValues()).toEqual({feature: 'b'});
    expect(state.getFlagValue('feature')).toEqual('b');
    expect(state.toJSON()).toEqual({
      feature: 'b',
      $flagsState: {
        feature: {
          version: 100,
          variation: 1,
          reason: { kind: 'OFF' },
          trackEvents: true,
          debugEventsUntilDate: 1000
        }
      },
      $valid: true
    });
  });

  it('can omit details for untracked flags', async () => {
    const flag1 = {
      key: 'flag1',
      version: 100,
      offVariation: 0,
      variations: ['value1']
    };
    const flag2 = {
      key: 'flag2',
      version: 200,
      offVariation: 0,
      variations: ['value2'],
      trackEvents: true
    };
    const flag3 = {
      key: 'flag3',
      version: 300,
      offVariation: 0,
      variations: ['value3'],
      debugEventsUntilDate: 1000
    };
    const td = TestData();
    td.usePreconfiguredFlag(flag1);
    td.usePreconfiguredFlag(flag2);
    td.usePreconfiguredFlag(flag3);
    
    const client = stubs.createClient({ updateProcessor: td });
    await client.waitForInitialization();
    const state = await client.allFlagsState(defaultUser, { withReasons: true, detailsOnlyForTrackedFlags: true });
    expect(state.valid).toEqual(true);
    expect(state.allValues()).toEqual({flag1: 'value1', flag2: 'value2', flag3: 'value3'});
    expect(state.getFlagValue('flag1')).toEqual('value1');
    expect(state.toJSON()).toEqual({
      flag1: 'value1',
      flag2: 'value2',
      flag3: 'value3',
      $flagsState: {
        flag1: {
          variation: 0
        },
        flag2: {
          version: 200,
          variation: 0,
          reason: { kind: 'OFF' },
          trackEvents: true
        },
        flag3: {
          version: 300,
          variation: 0,
          reason: { kind: 'OFF' },
          debugEventsUntilDate: 1000
        }
      },
      $valid: true
    });
  });

  it('returns empty state in offline mode and logs a message', async () => {
    const flag = {
      key: 'flagkey',
      on: false,
      offVariation: null
    };
    const td = TestData();
    td.usePreconfiguredFlag(flag);
    const logger = stubs.stubLogger();
    const client = stubs.createClient({ offline: true, logger: logger, updateProcessor: td });
    await client.waitForInitialization();
    const state = await client.allFlagsState(defaultUser);
    expect(state.valid).toEqual(false);
    expect(state.allValues()).toEqual({});
    expect(logger.info).toHaveBeenCalledTimes(1);
  });

  it('does not overflow the call stack when evaluating a huge number of flags', async () => {
    const flagCount = 5000;
    const td = TestData();
    for (let i = 0; i < flagCount; i++) {
      td.usePreconfiguredFlag({
        key: 'feature' + i,
        version: 1,
        on: false
      });
    }
    const client = stubs.createClient({ updateProcessor: td });
    await client.waitForInitialization();
    const state = await client.allFlagsState(defaultUser);
    expect(Object.keys(state.allValues()).length).toEqual(flagCount);
  });

  it('can use a callback instead of a Promise', done => {
    const client = stubs.createClient({ offline: true }, { });
    client.on('ready', () => {
      client.allFlagsState(defaultUser, {}, (err, state) => {
        expect(state.valid).toEqual(false);
        done();
      });
    });
  });

  it('can omit options parameter with callback', done => {
    const client = stubs.createClient({ offline: true }, { });
    client.on('ready', () => {
      client.allFlagsState(defaultUser, (err, state) => {
        expect(state.valid).toEqual(false);
        done();
      });
    });
  });
});
