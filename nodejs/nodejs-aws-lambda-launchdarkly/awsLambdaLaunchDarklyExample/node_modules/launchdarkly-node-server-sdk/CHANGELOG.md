# Change log

All notable changes to the LaunchDarkly Server-Side SDK for Node.js will be documented in this file. This project adheres to [Semantic Versioning](http://semver.org).

## [6.4.1] - 2022-04-26
### Fixed:
- When using polling mode (`stream: false`), if the SDK was shut down with `close()`, a timer task could still remain active for up to 30 seconds (or whatever the polling interval was set to).

## [6.4.0] - 2022-03-22
This release is functionally identical to the 6.3.1 release. It exists for compliance with semantic versioning, and to correct the changelog: the new feature described below was accidentally omitted from the changelog, and the minor version number should have been incremented.

### Added:
- `LDOptions.application`, for configuration of application metadata that may be used in LaunchDarkly analytics or other product features. This does not affect feature flag evaluations.

## [6.3.1] - 2022-03-10
### Fixed:
- Removed a transitive dependency on the package `url-parse`, which was flagged in [CVE-2022-0686](https://nvd.nist.gov/vuln/detail/CVE-2022-0686).

## [6.3.0] - 2022-02-18
### Added:
- `TestData`, in the new module `launchdarkly-node-server-sdk/integrations`, is a new way to inject feature flag data programmatically into the SDK for testing—either with fixed values for each flag, or with targets and/or rules that can return different values for different users. Unlike the file data source, this mechanism does not use any external resources, only the data that your test code has provided.
- `launchdarkly-node-server-sdk/integrations` is the new preferred module for importing `FileDataSource` and the `FileDataSourceOptions` type.

### Deprecated:
- The `FileDataSource` and `FileDataSourceOptions` exports from the main module are now deprecated.

## [6.2.3] - 2022-02-04
### Fixed:
- When using `allFlagsState` to produce bootstrap data for the JavaScript SDK, the Node SDK was not returning the correct metadata for evaluations that involved an experiment. As a result, the analytics events produced by the JavaScript SDK did not correctly reflect experimentation results.
- Referencing the user attribute `secondary` in a flag rule did not work.
- When using semantic version comparison operators, if one of the values was not a string, instead of correctly causing only that comparison to fail, it made the whole flag evaluation fail.
- The `allFlagsState` method was not logging any warning if it was called when the client was in an uninitialized state. It now logs a warning, consistent with the other SDKs.

## [6.2.2] - 2022-01-04
### Fixed:
- Some unsupported options were accidentally being passed to `http.request` and `https.request`. This did not affect Node itself, but it could cause problems when using tools that modify HTTP behavior with an interceptor, as discussed [here](https://github.com/mswjs/interceptors/issues/188).

## [6.2.1] - 2021-12-10
### Fixed:
- Replaced all uses of `for...in` for iterating over arrays and properties to use other constructs such as `for...of`. This makes the SDK more resilient in environments where application code or other libraries may have added new properties to the prototypes of standard JS types such as `Array`. (Thanks, [luke-schleicher](https://github.com/launchdarkly/node-server-sdk/pull/230)!)

## [6.2.0] - 2021-07-22
### Added:
- The SDK now supports evaluation of Big Segments. An Early Access Program for creating and syncing Big Segments from customer data platforms is available to enterprise customers.

## [6.1.0] - 2021-06-21
### Added:
- The SDK now supports the ability to control the proportion of traffic allocation to an experiment. This works in conjunction with a new platform feature now available to early access customers.

## [6.0.0] - 2021-06-17
This major version release is for updating Node.js compatibility, simplifying the SDK&#39;s dependencies, and removing deprecated names.

Except for the dependency changes described below which may require minor changes in your build, and a minor new logging feature, usage of the SDK has not changed in this release. For more details about changes that may be necessary, see the [5.x to 6.0 migration guide](https://docs.launchdarkly.com/sdk/server-side/node-js/migration-5-to-6).

Dropping support for obsolete Node.js versions makes it easier to maintain the SDK and keep its dependencies up to date. See LaunchDarkly&#39;s [End of Life Policy](https://launchdarkly.com/policies/end-of-life-policy/) regarding platform version support.

Simplifying dependencies reduces the size of the SDK bundle, as well as reducing potential compatibility problems and vulnerabilities. The total size of the SDK code plus its dependency tree, not including any of the optional database integrations, has been reduced by nearly 75%.

### Added:
- Added `ld.basicLogger`, allowing customization of the SDK&#39;s default logging behavior without having to provide a full `LDLogger` implementation.

### Changed:
- The minimum Node.js version is now 12.0.
- Updated many dependencies to newer versions and/or more actively maintained packages.

### Removed:
- Removed the bundled Redis integration. This is now provided as a separate package, like the other database integrations; see [node-server-sdk-redis](https://github.com/launchdarkly/node-server-sdk-redis). The main SDK package no longer has a dependency on the [`redis`](https://www.npmjs.com/package/redis) package.
- Removed the dependency on [Winston](https://www.npmjs.com/package/winston). You can still tell the SDK to use a Winston logger instance that you have created, just as before, so this change should not affect any applications that are using Winston. But the SDK no longer uses Winston to create a default logger if the application does not specify a logger; instead, it uses the `ld.basicLogger` implementation, which uses the same format as the previous default Winston configuration, so again there should be no visible difference. **Note:** If you are using the `launchdarkly-node-server-sdk-dynamodb` or `launchdarkly-node-server-consul` database integration packages, you should update them to the latest versions which have also had the Winston dependency removed.
- Removed the dependency on [`yaml`](https://www.npmjs.package/yaml). This package was only used for the optional [file data source](https://launchdarkly.github.io/node-server-sdk/index.html#filedatasource) functionality. You can still use YAML data files with the file data source if you explicitly install the `yaml` package in your project; the SDK will automatically detect its presence.
- The `package-lock.json` file is no longer in source control. As this is a library project, the lockfile never affected application code that used the SDK, but only affected the SDK&#39;s CI build. It is preferable for the CI build to refer only to `package.json` so that it resolves dependencies the same way an application using the SDK would, rather than using pinned dependencies that an application would not use.

## [5.14.5] - 2021-06-10
### Fixed:
- Updated transitive dependency on the package `url-parse` due to a [vulnerability warning](https://github.com/advisories/GHSA-9m6j-fcg5-2442).

## [5.14.4] - 2021-04-29
### Fixed:
- Updated the minimum compatible version of the `redis` package dependency to `3.1.1` to ensure the resolution of [CVE-2021-29469](https://github.com/advisories/GHSA-35q2-47q7-3pc3) in customers&#39; environments.

## [5.14.3] - 2021-04-01
### Fixed:
- The property `LDOptions.inlineUsersInEvents` was not included in the TypeScript definitions.

## [5.14.2] - 2021-02-11
### Fixed:
- Fixed an issue where &#34;feature&#34; and &#34;custom&#34; events were not properly indicated as being triggered by anonymous users. This bug affected customers who were using this functionality, which was introduced in version 5.14.0, when correlating users with LaunchDarkly&#39;s data export add-on.

## [5.14.1] - 2021-02-09
### Fixed:
- Removed a line that would have globally monkey-patched `setImmediate` on extremely old Node versions that did not implement that function. On all currently supported Node versions, this line had no effect. ([#206](https://github.com/launchdarkly/node-server-sdk/issues/206))

## [5.14.0] - 2021-01-29
### Added:
- Added the `alias` method to `LDClient`. This can be used to associate two user objects for analytics purposes with an alias event.

## [5.13.5] - 2021-01-25
### Changed:
- In streaming mode, the SDK now automatically drops and restarts the stream connection if it has received no data from the server within a 5-minute interval. This ensures that if the connection fails in such a way that the SDK cannot detect the failure as an I/O error, it will not hang forever waiting for updates from the phantom connection. The LaunchDarkly streaming service sends a tiny &#34;heartbeat&#34; message at regular intervals less than this timeout, to ensure that the SDK will not drop the connection if it is still usable. This logic exists in most other LaunchDarkly SDKs but was not previously implemented in the Node server-side SDK.

## [5.13.4] - 2020-08-27
### Fixed:
- When using the Redis integration without trying to connect to LaunchDarkly (such as when using the Relay Proxy with Redis in &#34;daemon mode&#34;), a flag evaluation that was done immediately after initializing the SDK client could fail due to the Redis client not having time to make its connection to Redis. This has been fixed so that it will wait for the connection to be established. This was never an issue when _not_ using daemon mode, because if the SDK did connect to LaunchDarkly then it would proceed to store the flags in Redis and would not finish initializing until it had done so. ([#193](https://github.com/launchdarkly/node-server-sdk/issues/193))

## [5.13.3] - 2020-06-30
### Fixed:
- A bug introduced in 5.13.2 caused the output of the default logger to be formatted incorrectly.


## [5.13.2] - 2020-06-30
### Changed:
- The default implementation of logging now uses Winston 3.x rather than Winston 2.x. This does not change the content of the log output, and if you have specified your own custom logger then the SDK still uses that. The only effect is that the SDK no longer has dependencies on Winston 2.x. (Thanks, [FauxFaux](https://github.com/launchdarkly/node-server-sdk/pull/189)!)

### Fixed:
- Previously, calling `LDClient.waitForInitialization()` multiple times _before_ the client had finished initializing would create a new Promise, and a new event listener, each time. This could cause unwanted overhead and possibly trigger warnings about too many event listeners. Now, `waitForInitialization()` will always return the same Promise instance and will not create multiple listeners.
- Fixed a bug that could cause extra delays when receiving a large streaming update. The process will still be blocked for some amount of time as the JSON data is being parsed, which is unavoidable in the current architecture, but this bug made it block for longer than necessary.
- Improved and clarified Typedoc documentation for several types and methods.


## [5.13.1] - 2020-04-24
### Changed:
- The `redis` package dependency has been updated to major version 3, which removes some deprecated usages (see [#184](https://github.com/launchdarkly/node-server-sdk/issues/184)) and adds support for `rediss:` URLs. This should not affect any application code even if the application is passing in a pre-built Redis client that was created with version 2.x, since the Redis methods that are used by the SDK have not changed.

### Fixed:
- If a proxy server was specified in the configuration, streaming connections still did not use the proxy. This bug was introduced in version 5.12.0. ([#186](https://github.com/launchdarkly/node-server-sdk/issues/186))
- The SDK could cause a crash if the application provided a configuration where `logger` was either not a logger at all (that is, some other object that did not have the methods defined in the `LDLogger` interface), or was a broken logger that could throw an exception while the SDK was trying to log an error. The former case (not a logger) is now treated as a severely invalid configuration, causing `LDClient.init()` to throw an exception just as it would if the SDK key were omitted. The latter (logger method throws an exception) is now handled by catching the exception and logging an error message to the _default_ console logger. (Thanks, [maxwellgerber](https://github.com/launchdarkly/node-server-sdk/pull/185)!)
- The Redis integration no longer calls `unref()` on the Redis client object after creating it. This was originally done to ensure that the SDK client would not prevent an application from exiting due to a still-open Redis connection, but that is no longer applicable since the application must close the SDK client anyway before exiting, which will cause the Redis client to be closed as well. The `unref()` call caused problems when running in Lambda, for unclear reasons. ([#76](https://github.com/launchdarkly/node-server-sdk/issues/76))

## [5.13.0] - 2020-04-07
### Added:
- Configuration option `streamInitialReconnectDelay`, which is the same as the recently-added `streamInitialReconnectDelayMillis` but measured in seconds instead of milliseconds; this makes it consistent with how durations are represented in other options.

### Deprecated:
- Configuration option `streamInitialReconnectDelayMillis`.

## [5.12.0] - 2020-04-03
### Added:
- New configuration option `streamInitialReconnectDelayMillis` allows setting the initial delay (not including jitter) for stream reconnections.

### Changed:
The stream reconnection behavior has been changed to be consistent with other LaunchDarkly SDKs, as follows:

- Previously, there was a fixed delay of 125 milliseconds for the first attempt to reconnect after a stream failure, which would then increase with an exponential backoff for subsbequent retries up to a maximum of 15 seconds (but the backoff was used only after HTTP error responses, not connection failures).
- Now, the delay starts at 1000 milliseconds, unless changed with `streamInitialReconnectDelayMillis`.
- The new maximum is 30 seconds.
- The backoff now applies to all types of stream failures.
- There is now a jitter that randomly reduces each delay by up to 50%.
- The delay now resets to the initial level if the stream has been active for at least 60 seconds.

## [5.11.3] - 2020-03-09
### Changed:
- The SDK no longer has dependencies on the deprecated `request` package and `request-etag`. Instead it uses the Node HTTP API directly.

### Fixed:
- Fixed a bug that, in polling mode, could cause polls to fail with the error &#34;Cannot read property &#39;data&#39; of undefined&#34; if the network became unavailable on the previous poll attempt but then became available again.


## [5.11.2] - 2020-02-20
### Changed:
- Updated the `request` package dependency due to a [vulnerability warning](https://www.sourceclear.com/vulnerability-database/security/prototype-pollution/javascript/sid-21913), even though the SDK was not using the functionality described in that warning.

## [5.11.1] - 2020-02-13
### Fixed:
- When sending stream connection statistics in diagnostic event data, always specify the `failed` property even if it is false. This only affects LaunchDarkly&#39;s internal analytics.

## [5.11.0] - 2020-02-12
Note: if you are using the LaunchDarkly Relay Proxy to forward events, update the Relay to version 5.10.0 or later before updating to this Node SDK version.

### Added:
- The SDK now periodically sends diagnostic data to LaunchDarkly, describing the version and configuration of the SDK, the architecture and version of the runtime platform, and performance statistics. No credentials, hostnames, or other identifiable values are included. This behavior can be disabled with the `diagnosticOptOut` option, or configured with `diagnosticRecordingInterval`.

### Fixed:
- Setting the `inlineUsersInEvents` option caused a spurious warning message saying it was an unknown option. This did not prevent it from working.
- Updated comment on `init` to clarify the intended singleton usage pattern.

### Removed:
- Removed an unnecessary dependency on the package `node-sha1`. ([#173](https://github.com/launchdarkly/node-server-sdk/issues/173))

## [5.10.3] - 2020-01-15
### Fixed:
- The SDK now specifies a uniquely identifiable request header when sending events to LaunchDarkly to ensure that events are only processed once, even if the SDK sends them two times due to a failed initial attempt.

## [5.10.2] - 2020-01-13
### Fixed:
- The implementation of the `RedisFeatureStore` function was inconsistent with its TypeScript declaration: instead of taking `client` as an optional parameter, it was looking for it as a property within `redisOpts`. It now correctly supports the optional parameter; passing the property in `redisOpts` is still supported for backward compatibility, but is deprecated (since it is not a valid property of that object type) and will be removed in a future version.

## [5.10.1] - 2020-01-06
### Fixed:
- In rare circumstances (depending on the exact data in the flag configuration, the flag's salt value, and the user properties), a percentage rollout could fail and return a default value, logging the error "Data inconsistency in feature flag ... variation/rollout object with no variation or rollout". This would happen if the user's hashed value fell exactly at the end of the last "bucket" (the last variation defined in the rollout). This has been fixed so that the user will get the last variation.

## [5.10.0] - 2019-12-11
### Added:
- `RedisFeatureStore` now accepts an optional `client` parameter, if you have an existing `RedisClient` instance that you want to reuse.

### Changed:
- The SDK now logs a warning if any configuration property has an inappropriate type, such as `baseUri:3` or `offline:"yes"`. For boolean properties, the SDK will still interpret the value in terms of truthiness, which was the previous behavior. For all other types, since there's no such commonly accepted way to coerce the type, it will fall back to the default setting for that property; previously, the behavior was undefined but most such mistakes would have caused the SDK to throw an exception at some later point.


## [5.9.2] - 2019-10-23
### Changed:
- Event listeners for `update` events were receiving an entire flag configuration object as an argument. This was not useful for applications (since the SDK does not provide any way to use such an object directly), and was unsafe since the object was shared with internal code and was mutable. The argument for `update` events is now an object with only one property, `key` (the flag key).
- The CI build will now fail if `npm audit` detects any vulnerabilities in the _runtime_ dependencies (not including `devDependencies`).
- The CI build now performs code linting.

### Fixed:
- There were significant performance bottlenecks due to the use of `setTimeout` to defer callbacks. These have been replaced with `setImmediate` for callbacks into application code, or with direct undeferred callbacks in internal code paths where it is safe to do so. (Thanks, [mdgbayly](https://github.com/launchdarkly/node-server-sdk/pull/150)!)
- When initializing the client, if it gets an HTTP error or network error that is considered retryable (as opposed to for instance a 401 error, which will not be retried), it should not reject the `waitForInitialization` promise and should not emit an `error` event. ([#155](https://github.com/launchdarkly/node-server-sdk/issues/155))
- Fixed some rule matching behavior for consistency with the other SDKs: string operators such as `startsWith` should only be able to match if both values are strings; numeric operators such as `lessThan` should only be able to match if both values are numbers; strings should only be usable as date/time values if they match RFC3339 format; and invalid regexes should simply cause a non-match, rather than an error.
- Previously, an `update` event would only be triggered for a flag if that flag's configuration were changed directly; it did not take into account indirect changes that could affect a flag's value, such as a change to one of its prerequisite flags or to a user segment that it references. This has been fixed so that any change potentially affecting a flag's value will produce an `update` event for that flag.
- The TypeScript declaration for `LDFeatureStore.upsert()` was wrong. (Thanks, [JakeGinnivan](https://github.com/launchdarkly/node-server-sdk/pull/160)!)
- Removed an unused dependency on the deprecated package `hoek`. ([#158](https://github.com/launchdarkly/node-server-sdk/issues/158))
- Updated the dependency on `semver`. (Thanks, [mmcgahan](https://github.com/launchdarkly/node-server-sdk/pull/151)!)
- Fixed various dependency versions that were flagged as vulnerable by `npm audit`.


## [5.9.1] - 2019-10-10
### Fixed:
- For an evaluation reason of `RULE_MATCH`, the `ruleIndex` property was always being set to zero rather than to the actual rule index. Note that it is always best to look at `ruleId` rather than `ruleIndex`, since it will never change even if rules are added or deleted.


## [5.9.0] - 2019-08-20
### Added:
- Added support for upcoming LaunchDarkly experimentation features. See `LDClient.track()`.

## [5.8.2] - 2019-06-06
### Fixed:
- Resolved a [low-severity security vulnerability](https://nvd.nist.gov/vuln/detail/CVE-2018-16492) in an `extend` transitive dependency.


## [5.8.1] - 2019-05-13
### Changed:
- Changed the package name from `ldclient-node` to `launchdarkly-node-server-sdk`.

There are no other changes in this release. Substituting `ldclient-node` version 5.8.0 with `launchdarkly-node-server-sdk` version 5.8.1 (and updating any `require` or `import` lines that referred to the old package name) will not affect functionality.

## [5.8.0] - 2019-04-06
### Added:
- Generated TypeDoc documentation for all types, properties, and methods is now available online at [https://launchdarkly.github.io/node-server-sdk/](https://launchdarkly.github.io/node-server-sdk/). Currently this will only be for the latest released version.
- It is now possible to specify any of the TLS configuration parameters supported by Node's `https.request()` in the client configuration, so that they will apply to all HTTPS requests made by the SDK. In your client options, add a property called `tlsParams` whose value is an object containing those parameters, e.g. `tlsParams: { ca: 'my trusted CA certificate data' }`.

### Fixed:
- Running the SDK unit tests is now simpler in that the Redis integration can be skipped. See `CONTRIBUTING.md`.

# Note on future releases

The LaunchDarkly SDK repositories are being renamed for consistency. This repository is now `node-server-sdk` rather than `node-client`. (Note that `node-client-sdk` also exists, which is the _client-side_ Node SDK.)

The package name will also change. In the 5.8.0 release, it is still `ldclient-node`; in all future releases, it will be `launchdarkly-node-server-sdk`. No further updates to the `ldclient-node` package will be published after this release.

## [5.7.4] - 2019-04-02
### Fixed:
- Setting user attributes to non-string values when a string was expected would cause analytics events not to be processed. The SDK will now convert attribute values to strings as needed. ([#147](https://github.com/launchdarkly/node-client/issues/147))
- If `track` or `identify` is called without a user, the SDK now logs a warning, and does not send an analytics event to LaunchDarkly (since it would not be processed without a user).


## [5.7.3] - 2019-03-21
### Changed:
- The default value for the configuration property `capacity` (maximum number of events that can be stored at once) is now 10000, consistent with the other SDKs, rather than 1000.

### Fixed:
- A missing `var` keyword could cause an error in strict mode when evaluating a flag with rollouts. (Thanks, [phillipb](https://github.com/launchdarkly/node-client/pull/145)!)
- The user attribute `secondary` was not included in the TypeScript declarations and therefore could not be used from TypeScript code.

## [5.7.2] - 2019-02-22
### Fixed:
- Calling `identify()` or `track()` with no user object, or with a user that has no key, will now cause the SDK to log a warning (as the other SDKs do). The SDK no longer sends an analytics event in this case, since LaunchDarkly would discard the event as invalid anyway. Also, previously, calling `identify()` with no user object would throw an exception.
- `FileDataSource`, in auto-update mode, could sometimes reload files more than once when they were only modified once (due to a known issue with Node's `fs.watch`). This should no longer happen. ([#138](https://github.com/launchdarkly/node-client/issues/138))
- Fixed dependency vulnerabilities flagged by `npm audit`. These were all for test-only dependencies, so would not affect production code.
- Previously, CI tests were only running on Linux. We have added a CI test suite that runs on Windows, using the latest stable version of Node.
- A supported user property, `privateAttributeNames`, was not usable from TypeScript because it was omitted from the TypeScript declarations.
- In TypeScript, asynchronous methods that can either take a callback or return a Promise were not usable in the Promise style, because the return types were declared incorrectly. ([#141](https://github.com/launchdarkly/node-client/issues/141))
- Some TypeScript declarations that used `type` now use `interface` instead, except for `LDFlagValue` which is a type alias. This should not affect regular usage of the SDK in TypeScript, but it is easier to extend an `interface` than a `type` if desired.

## [5.7.1] - 2019-01-16

Changes are only in test code used by other libraries. There is no need to upgrade to this release.

## [5.7.0] - 2019-01-11
### Added:
- It is now possible to inject feature flags into the client from local JSON or YAML files, replacing the normal LaunchDarkly connection. This would typically be for testing purposes. See `FileDataSource` in the [TypeScript API documentation](https://github.com/launchdarkly/node-client/blob/master/index.d.ts), and ["Reading flags from a file"](https://docs.launchdarkly.com/sdk/features/flags-from-files#nodejs-server-side).

### Fixed:
- Fixed a potential race condition that could happen when using a DynamoDB or Consul feature store. The Redis feature store was not affected.

## [5.6.2] - 2018-11-15
### Fixed:
- Creating multiple clients with the default in-memory feature store (i.e. leaving `config.featureStore` unset) was causing all of the clients to share the _same_ feature store instance. This has been fixed so they will now each get their own in-memory store. (Thanks, [seanparmelee](https://github.com/launchdarkly/node-client/pull/130)!)

## [5.6.1] - 2018-11-15
### Fixed:
- Fixed a bug introduced in v5.6.0 that could cause an unhandled promise rejection if a Redis error occurred while trying to query all flags from Redis.
- Fixed a bug introduced in v5.6.0 that could cause an exception when calling `close()` on a client that was using a Redis feature store _without_ in-memory caching.

## [5.6.0] - 2018-11-14
### Added:
- To make it easier to build feature store integrations for databases other than Redis, some of the feature store support logic has been made into a reusable component in `caching_store_wrapper.js`.

### Changed:
- For proxy support, the SDK now uses the published version of the `tunnel` package from NPM, rather than a Git reference to a fork.

## [5.5.0] - 2018-10-08
### Added:
- The `allFlagsState` method now accepts a new option, `detailsOnlyForTrackedFlags`, which reduces the size of the JSON representation of the flag state by omitting some metadata. Specifically, it omits any data that is normally used for generating detailed evaluation events if a flag does not have event tracking or debugging turned on.

### Fixed:
- Fixed an error that would occur in two cases where the client should return a default value: evaluating a flag when the client and the feature store are not yet initialized, and evaluating with no flag key. (Thanks, [SharkofMirkwood](https://github.com/launchdarkly/node-client/pull/123)!)
- JSON data from `allFlagsState` is now slightly smaller even if you do not use the new option described above, because it completely omits the flag property for event tracking unless that property is true.

## [5.4.2] - 2018-09-05
### Fixed:
- Fixed a bug that would sometimes cause an unhandled promise rejection warning-- and, depending on your Node configuration, a crash-- if there was an HTTP error during an automatic event flush. This was a partial regression of [#85](https://github.com/launchdarkly/node-client/issues/85) which was introduced in v5.0.0, although unlike the earlier bug, it happened nondeterministically rather than for all errors.

## [5.4.1] - 2018-09-05
### Fixed:
- Fixed a ReferenceError that occurred if a feature flag had invalid properties, e.g. a rule referred to a nonexistent variation index. Instead, an error will be written to the log and the flag will return the default value. ([#119](https://github.com/launchdarkly/node-client/issues/119))

## [5.4.0] - 2018-08-30
### Added:
- The new `LDClient` method `variationDetail` allows you to evaluate a feature flag (using the same parameters as you would for `variation`) and receive more information about how the value was calculated. This information is returned in an object that contains both the result value and a "reason" object which will tell you, for instance, if the user was individually targeted for the flag or was matched by one of the flag's rules, or if the flag returned the default value due to an error.

### Fixed:
- Evaluating a prerequisite feature flag did not produce an analytics event if the prerequisite flag was off.

## [5.3.2] - 2018-08-29
### Fixed:
- Fixed TypeScript syntax errors in `index.d.ts`. We are now running the TypeScript compiler in our automated builds to avoid such problems. (Thanks, [PsychicCat](https://github.com/launchdarkly/node-client/pull/116)!)

## [5.3.1] - 2018-08-27
### Fixed:
- Calling `allFlagsState()` did not work if you omitted the optional second parameter, `options`, but did provide a `callback`. ([#114](https://github.com/launchdarkly/node-client/issues/114))

## [5.3.0] - 2018-08-27
### Added:
- The new `LDClient` method `allFlagsState()` should be used instead of `allFlags()` if you are passing flag data to the front end for use with the JavaScript SDK. It preserves some flag metadata that the front end requires in order to send analytics events correctly. Versions 2.5.0 and above of the JavaScript SDK are able to use this metadata, but the output of `allFlagsState()` will still work with older versions.
- The `allFlagsState()` method also allows you to select only client-side-enabled flags to pass to the front end, by using the option `clientSideOnly: true`.

### Deprecated:
- `LDClient.allFlags()`

## [5.2.1] - 2018-08-22

### Fixed:
- Problematic dependencies flagged by `npm audit` have been fixed. Note that these were all development-only dependencies, so should not have affected any production code. ([#108](https://github.com/launchdarkly/node-client/issues/108))
- Type definitions for `LDFeatureStore` are now correct.
- Fixed an accidental global variable reference in `event_summarizer.js`. (Thanks, [jwenzler](https://github.com/launchdarkly/node-client/pull/111#pullrequestreview-148668257)!)

## [5.2.0] - 2018-08-01

### Changed:
- The promise from `waitForInitialization()`, if successful, now resolves with a value: the client. Previously, it resolved with no value. (Thanks, [rmanalan](https://github.com/launchdarkly/node-client/pull/106)!)

### Fixed:
- Receiving an HTTP 400 error from LaunchDarkly should not make the client give up on sending any more requests to LaunchDarkly (unlike a 401 or 403).

## [5.1.2] - 2018-07-26

### Removed:
- Removed a dependency on the deprecated [`crypto`](https://www.npmjs.com/package/crypto) module. ([#92](https://github.com/launchdarkly/node-client/issues/92))

## [5.1.1] - 2018-07-19

### Fixed:
- Now outputs a more descriptive log message if `allFlags` is called with a null user object. (Thanks, [jbatchelor-atlassian](https://github.com/launchdarkly/node-client/pull/103)!)
- Added TypeScript definitions for some previously undefined types.
- Updated `request` package dependency to `2.87.0`, to avoid a [security vulnerability](https://snyk.io/vuln/npm:cryptiles:20180710) in a package used by `request`.

## [5.1.0] - 2018-06-26

### Added:
- The new event `"failed"` will fire if client initialization failed due to any of the unrecoverable errors described below. If you prefer to use Promises, there is a new method `waitForInitialization()`, which behaves exactly like `waitUntilReady()` except that its Promise will be rejected if the "failed" event fires. (For backward compatibility, the Promise returned by `waitUntilReady()` will never be rejected.) ([#96](https://github.com/launchdarkly/node-client/issues/96))

### Changed:
- The client now treats most HTTP 4xx errors as unrecoverable: that is, after receiving such an error, it will not make any more HTTP requests for the lifetime of the client instance, in effect taking the client offline. This is because such errors indicate either a configuration problem (invalid SDK key) or a bug, which is not likely to resolve without a restart or an upgrade. This does not apply if the error is 400, 408, 429, or any 5xx error.

### Fixed:
- Fixed a bug that would cause a null reference error if you called `close()` on an offline client. (Thanks, [dylanlingelbach](https://github.com/launchdarkly/node-client/pull/100)!)

### Deprecated:
- The `waitUntilReady()` method is now deprecated in favor of `waitForInitialization()` (see above).

## [5.0.2] - 2018-06-15

### Fixed:
- Removed an indirect dependency on an old version of the `querystringify` module, which had a [security flaw](https://github.com/unshiftio/querystringify/pull/19). ([#97](https://github.com/launchdarkly/node-client/issues/97))
- Updated TypeScript definitions for client options. (Thanks, [stepanataccolade](https://github.com/launchdarkly/node-client/pull/95#pullrequestreview-126088214)!)

## [5.0.1] - 2018-05-31

### Fixed:
- Fixed a bug that caused summary events to combine two different counters: a) flag evaluations that produced the flag's first variation, and b) counts for flag evaluations that fell through to the default value.

### Removed:
- Removed debug-level logging that was listing every analytics event.

## [5.0.0] - 2018-05-10

### Changed:
- To reduce the network bandwidth used for analytics events, feature request events are now sent as counters rather than individual events, and user details are now sent only at intervals rather than in each event. These behaviors can be modified through the LaunchDarkly UI and with the new configuration option `inlineUsersInEvents`. For more details, see [Data Export](https://docs.launchdarkly.com/home/data-export).
- Pending analytics events are now flushed if 1. the configured `flush_interval` elapses or 2. you explicitly call `flush()`. Previously, if the number of events exceeded the configured capacity it would also trigger a flush; now, the client will simply drop events until the next timed or explicit flush occurs. This makes the Node SDK consistent with the other SDKs, and prevents unbounded use of network resources if you are generating analytics events rapidly.
- When sending analytics events, if there is a connection error or an HTTP 5xx response, the client will try to send the events again one more time after a one-second delay.
- In every function that takes an optional callback parameter, if you provide a callback, the function will not return a promise; a promise will be returned only if you omit the callback. Previously, it would always return a promise which would be resolved/rejected at the same time that the callback (if any) was called; this caused problems if you had not registered an error handler for the promise.

### Fixed:
- Removed a dependency on `hoek` v4.2.0, which had a [security flaw](https://nodesecurity.io/advisories/566); now uses 4.2.1 instead.

### Deprecated:
- All function and property names that used underscores are now deprecated; please use their camelCase equivalent instead (e.g. `allFlags` instead of `all_flags`). The deprecated names will still work for now, but will trigger a warning message in the log.


## [4.0.5] - 2018-05-03
### Fixed
- The waitUntilReady Promise will now resolve even after the ready event was emitted — thanks @dylanjha

## [4.0.4] - 2018-04-17
### Fixed
- Fixed a bug that could cause a call stack overflow when calling `all_flags` with a very large number of flags, or evaluating a flag with a very large number of rules. This should no longer happen no matter how many flags or rules there are.

## [4.0.3] - 2018-03-27
### Fixed
- Fixed a [bug](https://github.com/launchdarkly/node-client/issues/85) that would cause an unhandled promise rejection warning-- and, depending on your Node configuration, a crash-- if there was an HTTP error during an automatic event flush.

## [4.0.2] - 2018-03-14
### Fixed
- In the Redis feature store, fixed synchronization problems that could cause a feature flag update to be missed if several of them happened in rapid succession.

## [4.0.1] - 2018-03-09
### Fixed
- Any Redis connection failure will now be logged and will trigger reconnection attempts transparently. Previously, it caused an uncaught exception. Note that during a Redis outage, flag evaluations will use the last known value from the in-memory cache if available (if this cache was enabled with the `cache_ttl` parameter to `RedisFeatureStore`), or otherwise the default value.
- Fixed a bug in the Redis adapter that caused an error ("Transaction discarded because of previous errors") at startup time if there were either no feature flags or no user segments.
- Fixed a bug that caused a spurious Redis query for the key "launchdarkly:undefined".
- Fixed a bug that could cause analytics events not to be reported for feature flags that were evaluated due to being prerequisites of other flags.

## [4.0.0] - 2018-02-21
### Added
- Support for a new LaunchDarkly feature: reusable user segments.

### Changed
- The feature store interface has been changed to support user segment data as well as feature flags. Existing code that uses `RedisFeatureStore` should work as before, but custom feature store implementations will need to be updated.

## [3.4.0] - 2018-02-13
### Added
- Adds support for a future LaunchDarkly feature, coming soon: semantic version user attributes.

### Fixed
- When using a Redis feature store, if the client has not finished initializing but the store has already been populated, checking a feature flag will use the last known data from the store rather than returning the default value.
- For consistency with the other SDKs, it is no longer possible to compute rollouts based on a user attribute whose value is a floating-point number or a boolean. String and int attributes are allowed.

## [3.3.2] - 2018-01-31
### Fixed
- The TypeScript definition for the `all` method of `LDFeatureStore` is now correct — [#77](https://github.com/launchdarkly/node-client/issues/77)

## [3.3.1] - 2018-01-23
### Fixed
- Methods that expose a `Promise` interface now properly return the resolution or rejection value to the caller. #75

## [3.3.0] - 2018-01-19
### Added
- Support for [private user attributes](https://docs.launchdarkly.com/home/users/attributes#creating-private-user-attributes).

## [3.2.1] - 2017-12-13
### Fixed
- Only emit stream 401 errors once

## [3.2.0] - 2017-12-13
### Added
- New `send_events` option to control whether the SDK should send events back to LaunchDarkly or not. Defaults to `true`.
### Changed
- If the SDK gets a 401 from LaunchDarkly it will stop retrying to connect since there is no way for the
SDK key to become valid again.

## [3.1.0] - 2017-12-12
### Changed
- Asynchronous SDK methods now return a `Promise`; the SDK now supports both the Node.js error callback interface and the `Promise` interface. (https://github.com/launchdarkly/node-client/issues/58)
- The SDK now emits an `error` event. If no `error` event handler exists, errors will be logged using the configured logger. (https://github.com/launchdarkly/node-client/issues/55)
- The SDK now returns context-specific error objects to make it easier to handle errors from consumer code. (https://github.com/launchdarkly/node-client/issues/56)
- A new `update` event is available on the client to be notified whenever the SDK receives feature flag updates from LaunchDarkly.
### Fixed
- Callbacks to asynchronous methods are now always called asynchronously (to avoid [zalgo](http://blog.izs.me/post/59142742143/designing-apis-for-asynchrony)) (https://github.com/launchdarkly/node-client/issues/69)


## [3.0.15] - 2017-07-21
### Changed
- More consistent User-Agent header usage
### Added
- Release script


## [3.0.14] - 2017-06-26
### Fixed
- Fixed implicit any in typescript definition
### Changed
- Improved error logging when polling

## [3.0.13] - 2017-05-16
### Changed
- Update typescript definitions

## [3.0.12] - 2017-05-16
### Changed
- Improve compatibility with ts-node parser

## [3.0.11] - 2017-05-16
### Changed
- Fix incorrect typescript definition for init()

## [3.0.10] - 2017-05-16
### Changed
- Add typescript definitions

## [3.0.9] - 2017-05-01
### Changed
- Log shorter messages, with a stack trace, for known errors with messages

## [3.0.8] - 2017-03-10
### Changed
- Fixed a bug where all_flags would not display correct flag result for user

## [3.0.7] - 2017-01-16
### Changed
- Fixed a bug in initialization that caused high CPU usage

## [3.0.6] - 2016-12-19
### Fixed
- Bug fix for receiving updates to large feature flags

## [3.0.5] - 2016-10-20
### Fixed
- Numerous bug fixes for the Redis feature store

## [3.0.4] - 2016-10-19
### Fixed
- The event queue is no longer a global property, so multiple clients initialized in one node process now send events to the correct environment

## [3.0.3] - 2016-09-09
### Added
- The `RedisFeatureStore` now takes an optional prefix parameter
### Fixed
- Mark the client initialized immediately in LDD mode

## [3.0.2] - 2016-08-19
### Fixed
- Fixed a bug in the secure_mode_hash function

## [3.0.1] - 2016-08-18
### Changed
- The `ready` event now gets properly emitted in offline mode.

## [3.0.0] - 2016-08-08
### Added
- Support for multivariate feature flags. In addition to booleans, feature flags can now return numbers, strings, dictionaries, or arrays via the `variation` method.
- New `all_flags` method returns all flag values for a specified user.
- New `secure_mode_hash` function computes a hash suitable for the new LaunchDarkly JavaScript client's secure mode feature.
- New `initialized` function returns whether or not the client has finished initialization.

### Deprecated
- The `toggle` call has been deprecated in favor of `variation`.
