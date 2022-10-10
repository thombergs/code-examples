#!/bin/bash

# As part of every release build, we always want to rebuild the polyfill code as well. This is a code
# change that will be committed during the release along with the usual version number update.

set -ue

echo "Using npm $(npm --version)"

# Install dependencies
echo; echo "Running npm install"
npm install || { echo "npm install failed" >&2; exit 1; }

echo; echo "Building polyfill"
npm run polyfill
