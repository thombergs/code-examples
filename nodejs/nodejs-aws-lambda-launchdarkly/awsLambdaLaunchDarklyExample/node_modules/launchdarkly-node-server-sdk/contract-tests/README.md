# SDK contract test service

This directory contains an implementation of the cross-platform SDK testing protocol defined by https://github.com/launchdarkly/sdk-test-harness. See that project's `README` for details of this protocol, and the kinds of SDK capabilities that are relevant to the contract tests. This code should not need to be updated unless the SDK has added or removed such capabilities.

To run these tests locally, run `npm run contract-tests` from the SDK project root directory. This will start the test service, download the correct version of the test harness tool, and run the tests.

Or, to test against an in-progress local version of the test harness, run `npm run contract-test-service` from the SDK project root directory; then, in the root directory of the `sdk-test-harness` project, build the test harness and run it from the command line.
