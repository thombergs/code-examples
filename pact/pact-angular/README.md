# Angular Pact example

This example project shows how to setup an Angular application to use [Pact](http://pact.io)
in order to create Pact files from a consumer test and validate the
a consumer against the Pact.

## Relevant Blog Post
[Creating a Consumer-Driven Contract with Angular and Pact](https://reflectoring.io/consumer-driven-contracts-with-angular-and-pact/)

## Key Files

* [`user.service.ts`](src/app/user.service.ts): Angular service that calls a REST
  backend to manage users
* [`user.service.pact.spec.ts`](src/app/user.service.pact.spec.ts): Jasmine-based
  consumer test of `user.service.ts`
* [`karma.conf.js`](karma.conf.js): configuration of the Karma test runner including
  configuration to setup Pact

## How to Run

Run `npm install` to load the needed javascript libraries and then `npm run test` to
run the tests. After the tests have successfully run the created pact file will be
created in the folder `pacts`. 

Then, you can call `npm run publish-pacts` to publish the pact files to a [Pact Broker](https://github.com/pact-foundation/pact_broker).
You must set the following npm configs for the `publish-pacts` script to work:

```
npm config set angular-pact:brokerUrl <URL>
npm config set angular-pact:brokerUsername <USER>
npm config set angular-pact:brokerPassword <PASS>

``` 
