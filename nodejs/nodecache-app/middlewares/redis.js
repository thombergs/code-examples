const { createClient } = require("redis");
const hash = require("object-hash");
let redisClient;

async function initializeRedisClient() {
  try {
    redisClient = createClient();
    await redisClient.connect();
    console.log("Redis Connected Successfully");
  } catch (e) {
    console.error(`Redis connection failed with error:`);
    console.error(e);
  }
}

function generateCacheKey(req, method = "GET") {
  let type = method.toUpperCase();
  // build a custom object to use as a part of our Redis key
  const reqDataToHash = {
    query: req.query,
  };
  return `${type}-${req.path}/${hash.sha1(reqDataToHash)}`;
}

function cacheMiddleware(
  options = {
    EX: 10800, // 3h
  }
) {
  return async (req, res, next) => {
    if (redisClient?.isOpen) {
      const key = generateCacheKey(req, req.method);

      //if cached data is found retrieve it
      const cachedValue = await redisClient.get(key);

      if (cachedValue) {
        return res.json(JSON.parse(cachedValue));
      } else {
        const oldSend = res.send;

        // When the middleware function redisCachingMiddleware is executed, it replaces the res.send function with a custom function.
        res.send = async function saveCache(data) {
          res.send = oldSend;

          // cache the response only if it is successful
          if (res.statusCode >= 200 && res.statusCode < 300) {
            await redisClient.set(key, data, options);
          }

          return res.send(data);
        };

        // continue to the controller function
        next();
      }
    } else {
      next();
    }
  };
}

function invalidateCacheMiddleware(req, res, next) {
  // Invalidate the cache for the cache key
  const key = generateCacheKey(req);
  redisClient.del(key);
  next();
}

module.exports = {
  initializeRedisClient,
  cacheMiddleware,
  invalidateCacheMiddleware,
};
