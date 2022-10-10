const async = require('async');

// The safeAsync functions allow us to use async collection functions as efficiently as possible
// while avoiding stack overflows. When the async utilities call our iteratee, they provide a
// callback for delivering a result. Calling that callback directly is efficient (we are not
// really worried about blocking a thread by doing too many computations without yielding; our
// flag evaluations are pretty fast, and if we end up having to do any I/O, that will cause us
// to yield anyway)... but, if there are many items in the collection, it will result in too
// many nested calls. So, we'll pick an arbitrary threshold of how many items can be in the
// collection before we switch over to deferring the callbacks with setImmediate().

const maxNestedCalls = 50;

function safeIteratee(collection, iteratee) {
  if (!collection || collection.length <= maxNestedCalls) {
    return iteratee;
  }
  return (value, callback) => iteratee(value, (...args) => setImmediate(callback, ...args));
}

function safeAsyncEach(collection, iteratee, resultCallback) {
  return async.each(collection, safeIteratee(collection, iteratee), resultCallback);
}

function safeAsyncEachSeries(collection, iteratee, resultCallback) {
  return async.eachSeries(collection, safeIteratee(collection, iteratee), resultCallback);
}

module.exports = {
  safeAsyncEach: safeAsyncEach,
  safeAsyncEachSeries: safeAsyncEachSeries,
};
