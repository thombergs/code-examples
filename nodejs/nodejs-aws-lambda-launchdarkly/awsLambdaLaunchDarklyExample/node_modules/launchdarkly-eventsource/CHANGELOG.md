# Change log

All notable changes to this package will be documented in this file.

## [1.4.3] - 2022-01-10
This release fixes a number of SSE spec compliance issues which do not affect usage in the LaunchDarkly SDKs, but could be relevant in other use cases.

### Fixed:
- If an event's `id:` field contains a null character, the whole field should be ignored.
- The parser was incorrectly ignoring lines that did not contain a colon, instead of treating them as a field with an empty value. For instance, `data` on a line by itself should be equivalent to `data:`.
- The parser should ignore any incomplete messages at the end of a stream if the stream disconnects.

## [1.4.2] - 2022-01-04
### Fixed:
- If the stream URL contained user/password basicauth fields, they were not being included in the request.
- Some unsupported options were accidentally being passed to `http.request` and `https.request`. This did not affect Node itself, but it could cause problems when using interceptors that inspect the options, as discussed [here](https://github.com/mswjs/interceptors/issues/188).

## [1.4.1] - 2021-05-10
### Fixed:
- Updated the dependency on the package `original` in order to pick up a newer version of the transitive dependency `url-parse`. Older versions of `url-parse` had a [known vulnerability](https://github.com/advisories/GHSA-9m6j-fcg5-2442). (Thanks, [m-schrepel](https://github.com/launchdarkly/js-eventsource/pull/11)!)

## [1.4.0] - 2021-01-25
### Added:
- Added `readTimeoutMillis` option for automatically dropping and restarting a connection if too much time has elapsed without receiving any data.

## [1.3.1] - 2020-06-29
### Fixed:
- Incorporated [a fix](https://github.com/EventSource/eventsource/pull/130) from the upstream repository that avoids unnecessary delays when parsing a long message that is received in multiple chunks.

## [1.3.0] - 2020-04-23
### Added:
- A Node.js `http.Agent` can be specified using the `agent` option.

## [1.2.0] - 2020-04-03
### Added:
- New configuration options: `errorFilter` (determines how errors should be handled), `initialRetryDelayMillis` (delay interval for connection retries), `jitterRatio` (enables delay jitter), `maxBackoffMillis` (enables exponential backoff), `retryResetIntervalMillis` (enables reset of backoff).
- New event types: `end` (the server has closed the connection), `retrying` (provides information about upcoming connection retries).
- See `README.md` for more about these features.
This project adheres to [Semantic Versioning](http://semver.org).

## [1.1.0] - 2019-07- 09

### Added:
- The new option `skipDefaultHeaders`, if set to true, makes EventSource _not_ add the `Accept` and `Cache-Control` request headers that it would normally add. This may be necessary to avoid CORS problems in browsers if the stream URL is in another domain, since there are more restrictions on cross-origin requests that contain these headers.
- There is a new property, `EventSource.supportedOptions`, that indicates which custom options are available. See "Detecting supported features" in [`README.md`](README.md#detecting-supported-features).

## [1.0.0] - 2019-01-29
First release from this fork. Changes from the previous release of the upstream code (1.0.7) are as follows:

### Added:
- The optional `method` and `body` properties of the constructor options allow you to specify a different HTTP method from the default of `GET`, and to provide a request body if the specified method allows a body.

### Changed:
- The EventSource constructor is now a named export, not a default export. This is necessary in order to avoid a problem that  can happen when using Babel with ES6 code: the logic for converting CJS imports to ES6 imports does not work correctly if the default import has properties (`CONNECTING`, `OPEN`, `CLOSED`) but is also a function. Note that this is a breaking change if you were previously using the upstream code, but the only thing that needs to be changed is the import statement.
