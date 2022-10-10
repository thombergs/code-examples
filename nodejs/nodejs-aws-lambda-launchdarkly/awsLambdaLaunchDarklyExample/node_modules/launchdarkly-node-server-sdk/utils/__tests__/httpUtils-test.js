const httpUtils = require('../httpUtils');
const packageJson = require('../../package.json');

it('sets SDK key', () => {
  const h = httpUtils.getDefaultHeaders('my-sdk-key', {});
  expect(h).toMatchObject({ authorization: 'my-sdk-key' });
});

it('sets user agent', () => {
  const h = httpUtils.getDefaultHeaders('my-sdk-key', {});
  expect(h).toMatchObject({ 'user-agent': 'NodeJSClient/' + packageJson.version });
});

it('does not include wrapper header by default', () => {
  const h = httpUtils.getDefaultHeaders('my-sdk-key', {});
  expect(h['x-launchdarkly-wrapper']).toBeUndefined();
});

it('sets wrapper header with name only', () => {
  const h = httpUtils.getDefaultHeaders('my-sdk-key', { wrapperName: 'my-wrapper' });
  expect(h).toMatchObject({ 'x-launchdarkly-wrapper': 'my-wrapper' });
});

it('sets wrapper header with name and version', () => {
  const h = httpUtils.getDefaultHeaders('my-sdk-key', { wrapperName: 'my-wrapper', wrapperVersion: '2.0' });
  expect(h).toMatchObject({ 'x-launchdarkly-wrapper': 'my-wrapper/2.0' });
});

it('sets the X-LaunchDarkly-Tags header with valid tags.', () => {
  const h = httpUtils.getDefaultHeaders('my-sdk-key', {
    application: {
      id: 'test-application',
      version: 'test-version',
    },
  });
  expect(h).toMatchObject({
    'x-launchdarkly-tags': 'application-id/test-application application-version/test-version',
  });
});
