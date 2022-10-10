// Encapsulation of configurable backoff/jitter behavior.
//
// - The system can either be in a "good" state or a "bad" state. The initial state is "bad"; the
// caller is responsible for indicating when it transitions to "good". When we ask for a new retry
// delay, that implies the state is now transitioning to "bad".
//
// - There is a configurable base delay, which can be changed at any time (if the SSE server sends
// us a "retry:" directive).
//
// - There are optional strategies for applying backoff and jitter to the delay.

function RetryDelayStrategy (baseDelayMillis, resetIntervalMillis, backoff, jitter) {
  var currentBaseDelay = baseDelayMillis
  var retryCount = 0
  var goodSince
  return {
    nextRetryDelay: function (currentTimeMillis) {
      if (goodSince && resetIntervalMillis && (currentTimeMillis - goodSince >= resetIntervalMillis)) {
        retryCount = 0
      }
      goodSince = null
      var delay = backoff ? backoff(currentBaseDelay, retryCount) : currentBaseDelay
      retryCount++
      return jitter ? jitter(delay) : delay
    },

    setGoodSince: function (goodSinceTimeMillis) {
      goodSince = goodSinceTimeMillis
    },

    setBaseDelay: function (baseDelay) {
      currentBaseDelay = baseDelay
      retryCount = 0
    }
  }
}

function defaultBackoff (maxDelayMillis) {
  return function (baseDelayMillis, retryCount) {
    var d = baseDelayMillis * Math.pow(2, retryCount)
    return d > maxDelayMillis ? maxDelayMillis : d
  }
}

function defaultJitter (ratio) {
  return function (computedDelayMillis) {
    return computedDelayMillis - Math.trunc(Math.random() * ratio * computedDelayMillis)
  }
}

module.exports = {
  RetryDelayStrategy: RetryDelayStrategy,
  defaultBackoff: defaultBackoff,
  defaultJitter: defaultJitter
}
