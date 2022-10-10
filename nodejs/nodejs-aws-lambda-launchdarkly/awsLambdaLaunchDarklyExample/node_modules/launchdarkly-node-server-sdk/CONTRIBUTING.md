# Contributing to the LaunchDarkly Server-Side SDK for Node.js

LaunchDarkly has published an [SDK contributor's guide](https://docs.launchdarkly.com/sdk/concepts/contributors-guide) that provides a detailed explanation of how our SDKs work. See below for additional information on how to contribute to this SDK.

## Submitting bug reports and feature requests

The LaunchDarkly SDK team monitors the [issue tracker](https://github.com/launchdarkly/node-server-sdk/issues) in the SDK repository. Bug reports and feature requests specific to this SDK should be filed in this issue tracker. The SDK team will respond to all newly filed issues within two business days.

## Submitting pull requests

We encourage pull requests and other contributions from the community. Before submitting pull requests, ensure that all temporary or unintended code is removed. Don't worry about adding reviewers to the pull request; the LaunchDarkly SDK team will add themselves. The SDK team will acknowledge all pull requests within two business days.

## Build instructions

### Prerequisites

The project should be built and tested against the lowest compatible version, Node 12. It uses `npm`, which is bundled in all supported versions of Node.

### Setup

To install project dependencies, from the project root directory:

```
npm install
```

### Testing

To run all unit tests:

```
npm test
```

To verify that the TypeScript declarations compile correctly (this involves compiling the file `test-types.ts`, so if you have changed any types or interfaces, you will want to update that code):

```
npm run check-typescript
```

To run the SDK contract test suite (see [`contract-tests/README.md`](./contract-tests/README.md)):

```bash
npm run contract-tests
```

### Auditing package dependencies

The `npm audit` tool compares all dependencies and transitive dependencies to a database of package versions with known vulnerabilities. However, the output of this tool includes both runtime and development dependencies.

Runtime dependencies can affect applications using the SDK; they can only be fixed by updating one of the explicit dependencies in `package.json`. Development dependencies cannot affect applications, but will still cause `npm audit` to flag the project; they can be fixed by running `npm audit fix` to add overrides for transitive dependencies in `package-lock.json`.

It is important _not_ to run `npm audit fix` if there are any bad _runtime_ dependencies, because it will hide the problem in our own build, without actually fixing the vulnerability when an application uses the SDK.

The script `scripts/better-audit.sh`, which is run in the CI build and can also be run manually, processes the output of `npm audit` to eliminate all duplicate entries and then determines whether each entry is coming from a runtime dependency or a development dependency. If there are any runtime ones, it terminates with an error code so the build will fail.
