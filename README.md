# Example Code Repository

[![Travis CI Status](https://travis-ci.org/thombergs/code-examples.svg?branch=master)](https://travis-ci.org/thombergs/code-examples)

This repo contains example projects which show how to use different (not only) Java technologies.
The examples are usually accompanied by a blog post on [https://reflectoring.io](https://reflectoring.io).

See the READMEs in each subdirectory of this repo for more information on each module.

## Java Modules
All Java modules require **Java 11** to compile and run.

### Building with Gradle

Each module should be an independent build and can be built by calling `./gradlew clean build` in the module directory. 

All modules are listed in [build-all.sh](build-all.sh) to run in the CI pipeline.

### Non-Java Modules

Some folders contain non-Java projects. For those, refer to the README within the module folder.
