
// This file exists only so that we can run the TypeScript compiler in the CI build
// to validate our index.d.ts file. This code will not actually be run - the point is
// just to verify that the type declarations exist and are correct so a TypeScript
// developer can use all of the SDK features.

import * as ld from 'launchdarkly-node-server-sdk';
import * as integrations from 'launchdarkly-node-server-sdk/integrations';
import * as interfaces from 'launchdarkly-node-server-sdk/interfaces';

var logger: ld.LDLogger = {
  error: (...args) => { },
  warn: (...args) => { },
  info: (...args) => { },
  debug: (...args) => { }
};
var emptyOptions: ld.LDOptions = {};
var allOptions: ld.LDOptions = {
  baseUri: '',
  eventsUri: '',
  streamUri: '',
  stream: true,
  streamInitialReconnectDelay: 1.5,
  sendEvents: true,
  allAttributesPrivate: true,
  privateAttributeNames: [ 'x' ],
  inlineUsersInEvents: true,
  capacity: 100,
  flushInterval: 1,
  userKeysCapacity: 100,
  userKeysFlushInterval: 1,
  pollInterval: 5,
  timeout: 1,
  logger: logger,
  tlsParams: {
    ca: 'x',
    cert: 'y',
    key: 'z'
  },
  diagnosticOptOut: true,
  diagnosticRecordingInterval: 100,
  wrapperName: 'x',
  wrapperVersion: 'y',
  application: {
    id: 'test-id',
    version: 'test-version'
  }
};
var userWithKeyOnly: ld.LDUser = { key: 'user' };
var anonymousUser: ld.LDUser = { key: 'anon-user', anonymous: true };
var user: ld.LDUser = {
  key: 'user',
  name: 'name',
  secondary: 'otherkey',
  firstName: 'first',
  lastName: 'last',
  email: 'test@example.com',
  avatar: 'http://avatar.url',
  ip: '1.1.1.1',
  country: 'us',
  anonymous: true,
  custom: {
    'a': 's',
    'b': true,
    'c': 3,
    'd': [ 'x', 'y' ],
    'e': [ true, false ],
    'f': [ 1, 2 ]
  },
  privateAttributeNames: [ 'name', 'email' ]
};
var client: ld.LDClient = ld.init('sdk-key', allOptions);

client.identify(user);
client.track('key', user);
client.track('key', user, { ok: 1 });
client.track('key', user, null, 1.5);

client.alias(user, anonymousUser);

// evaluation methods with callbacks
client.variation('key', user, false, (value: ld.LDFlagValue) => { });
client.variation('key', user, 2, (value: ld.LDFlagValue) => { });
client.variation('key', user, 'default', (value: ld.LDFlagValue) => { });
client.variationDetail('key', user, 'default', (detail: ld.LDEvaluationDetail) => {
  var detailValue: ld.LDFlagValue = detail.value;
  var detailIndex: number | undefined = detail.variationIndex;
  var detailReason: ld.LDEvaluationReason = detail.reason;  
});
client.allFlagsState(user, {}, (err: Error, flagSet: ld.LDFlagsState) => { });

// evaluation methods with promises
client.variation('key', user, false).then((value: ld.LDFlagValue) => { });
client.variation('key', user, 2).then((value: ld.LDFlagValue) => { });
client.variation('key', user, 'default').then((value: ld.LDFlagValue) => { });
client.variationDetail('key', user, 'default').then((detail: ld.LDEvaluationDetail) => { });
client.allFlagsState(user).then((flagSet: ld.LDFlagsState) => { });

// basicLogger
var logger1: ld.LDLogger = ld.basicLogger();
var logger2: ld.LDLogger = ld.basicLogger({ level: 'info' });
var logger3: ld.LDLogger = ld.basicLogger({ destination: console.log });

// integrations module:

// FileDataSource
const fdsOptions: integrations.FileDataSourceOptions = {
  paths: [ 'filepath' ],
  autoUpdate: true,
  logger: ld.basicLogger(),
};
const fds = integrations.FileDataSource(fdsOptions);

// TestData
const td: integrations.TestData = integrations.TestData();
const fb: integrations.TestDataFlagBuilder = td.flag('key');
td.update(fb);
fb.ifMatch('name', 'x').thenReturn(true);

// interfaces module:

// BigSegmentStoreStatusProvider
var bsssp: interfaces.BigSegmentStoreStatusProvider = client.bigSegmentStoreStatusProvider
var bssStatus: interfaces.BigSegmentStoreStatus | undefined = bsssp.getStatus();
bsssp.requireStatus().then((value: interfaces.BigSegmentStoreStatus) => { });
