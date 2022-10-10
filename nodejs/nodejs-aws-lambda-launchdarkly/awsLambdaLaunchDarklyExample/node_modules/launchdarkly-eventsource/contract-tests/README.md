# SSE client contract test service

This directory contains an implementation of the cross-platform SSE testing protocol defined by https://github.com/launchdarkly/sse-contract-tests. See that project's `README` for details of this protocol, and the kinds of SSE client capabilities that are relevant to the contract tests. This code should not need to be updated unless the SSE client has added or removed such capabilities.

To run these tests locally, run `make contract-tests` from the project root directory. This downloads the correct version of the test harness tool automatically.
