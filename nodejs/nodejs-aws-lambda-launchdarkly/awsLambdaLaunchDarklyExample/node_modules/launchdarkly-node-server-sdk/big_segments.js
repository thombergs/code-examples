const { createHash } = require('crypto');
const { EventEmitter } = require('events');
const LRUCache = require('lru-cache');

const defaultStaleAfter = 120;
const defaultStatusPollInterval = 5;
const defaultUserCacheSize = 1000;
const defaultUserCacheTime = 5;
const emptyMembership = {};

function BigSegmentStoreManager(store, config, logger) {
  const staleTimeMs = (config.staleAfter > 0 ? config.staleAfter : defaultStaleAfter) * 1000;
  const pollIntervalMs = (config.statusPollInterval > 0 ? config.statusPollInterval : defaultStatusPollInterval) * 1000;
  const pollTask = store ? setInterval(() => pollStoreAndUpdateStatus(), pollIntervalMs) : null;
  const cache = store
    ? new LRUCache({
        max: config.userCacheSize || defaultUserCacheSize,
        maxAge: (config.userCacheTime || defaultUserCacheTime) * 1000,
      })
    : null;
  let lastStatus;

  const ret = {};

  ret.close = () => {
    clearInterval(pollTask);
    store && store.close && store.close();
  };

  const statusProvider = new EventEmitter();
  ret.statusProvider = statusProvider;
  statusProvider.getStatus = () => lastStatus;
  statusProvider.requireStatus = async () => {
    if (!lastStatus) {
      await pollStoreAndUpdateStatus();
    }
    return lastStatus;
  };

  // Called by the evaluator when it needs to get the Big Segment membership state for a user.
  //
  // If there is a cached membership state for the user, it returns the cached state. Otherwise,
  // it converts the user key into the hash string used by the BigSegmentStore, queries the store,
  // and caches the result.
  //
  // The return value is a two-element array where the first element is the membership object,
  // and the second element is a status value ("HEALTHY", "STALE", or "STORE_ERROR"). An undefined
  // return value is equivalent to [ null, "NOT_CONFIGURED" ];
  ret.getUserMembership = async userKey => {
    if (!store) {
      return undefined;
    }
    let membership = cache.get(userKey);
    if (!membership) {
      try {
        membership = await store.getUserMembership(hashForUserKey(userKey));
        if (membership === null || membership === undefined) {
          membership = emptyMembership;
        }
        cache.set(userKey, membership);
      } catch (e) {
        logger.error('Big Segment store membership query returned error: ' + e);
        return [null, 'STORE_ERROR'];
      }
      cache.set(userKey, membership);
    }
    if (!lastStatus) {
      await pollStoreAndUpdateStatus();
    }
    if (!lastStatus.available) {
      return [membership, 'STORE_ERROR'];
    }
    return [membership, lastStatus.stale ? 'STALE' : 'HEALTHY'];
  };

  async function pollStoreAndUpdateStatus() {
    if (!store) {
      lastStatus = { available: false, stale: false };
      return;
    }
    logger.debug('Querying Big Segment store status');
    let newStatus;
    try {
      const metadata = await store.getMetadata();
      newStatus = { available: true, stale: !metadata || !metadata.lastUpToDate || isStale(metadata.lastUpToDate) };
    } catch (e) {
      logger.error('Big Segment store status query returned error: ' + e);
      newStatus = { available: false, stale: false };
    }
    if (!lastStatus || lastStatus.available !== newStatus.available || lastStatus.stale !== newStatus.stale) {
      logger.debug(
        'Big Segment store status changed from %s to %s',
        JSON.stringify(lastStatus),
        JSON.stringify(newStatus)
      );
      lastStatus = newStatus;
      statusProvider.emit('change', newStatus);
    }
  }

  function isStale(timestamp) {
    return new Date().getTime() - timestamp >= staleTimeMs;
  }

  return ret;
}

function hashForUserKey(userKey) {
  const hasher = createHash('sha256');
  hasher.update(userKey);
  return hasher.digest('base64');
}

module.exports = {
  BigSegmentStoreManager,
  hashForUserKey,
};
