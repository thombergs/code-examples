/*
 * node-cache 5.1.2 ( 2020-07-01 )
 * https://github.com/node-cache/node-cache
 *
 * Released under the MIT license
 * https://github.com/node-cache/node-cache/blob/master/LICENSE
 *
 * Maintained by  (  )
*/
(function() {
  var EventEmitter, NodeCache, clone,
    splice = [].splice,
    boundMethodCheck = function(instance, Constructor) { if (!(instance instanceof Constructor)) { throw new Error('Bound instance method accessed before binding'); } },
    indexOf = [].indexOf;

  clone = require("clone");

  EventEmitter = require('events').EventEmitter;

  // generate superclass
  module.exports = NodeCache = (function() {
    class NodeCache extends EventEmitter {
      constructor(options = {}) {
        super();
        // ## get

        // get a cached key and change the stats

        // **Parameters:**

        // * `key` ( String | Number ): cache key

        // **Example:**

        //	myCache.get "myKey", ( err, val )

        this.get = this.get.bind(this);
        // ## mget

        // get multiple cached keys at once and change the stats

        // **Parameters:**

        // * `keys` ( String|Number[] ): an array of keys

        // **Example:**

        //	myCache.mget [ "foo", "bar" ]

        this.mget = this.mget.bind(this);
        // ## set

        // set a cached key and change the stats

        // **Parameters:**

        // * `key` ( String | Number ): cache key
        // * `value` ( Any ): A element to cache. If the option `option.forceString` is `true` the module trys to translate it to a serialized JSON
        // * `[ ttl ]` ( Number | String ): ( optional ) The time to live in seconds.

        // **Example:**

        //	myCache.set "myKey", "my_String Value"

        //	myCache.set "myKey", "my_String Value", 10

        this.set = this.set.bind(this);
        
        // ## mset

        // set multiple keys at once

        // **Parameters:**

        // * `keyValueSet` ( Object[] ): an array of object which includes key,value and ttl

        // **Example:**

        //	myCache.mset(
        //		[
        //			{
        //				key: "myKey",
        //				val: "myValue",
        //				ttl: [ttl in seconds]
        //			}
        //		])

        this.mset = this.mset.bind(this);
        // ## del

        // remove keys

        // **Parameters:**

        // * `keys` ( String | Number | String|Number[] ): cache key to delete or a array of cache keys

        // **Return**

        // ( Number ): Number of deleted keys

        // **Example:**

        //	myCache.del( "myKey" )

        this.del = this.del.bind(this);
        // ## take

        // get the cached value and remove the key from the cache.
        // Equivalent to calling `get(key)` + `del(key)`.
        // Useful for implementing `single use` mechanism such as OTP, where once a value is read it will become obsolete.

        // **Parameters:**

        // * `key` ( String | Number ): cache key

        // **Example:**

        //	myCache.take "myKey", ( err, val )

        this.take = this.take.bind(this);
        // ## ttl

        // reset or redefine the ttl of a key. `ttl` = 0 means infinite lifetime.
        // If `ttl` is not passed the default ttl is used.
        // If `ttl` < 0 the key will be deleted.

        // **Parameters:**

        // * `key` ( String | Number ): cache key to reset the ttl value
        // * `ttl` ( Number ): ( optional -> options.stdTTL || 0 ) The time to live in seconds

        // **Return**

        // ( Boolen ): key found and ttl set

        // **Example:**

        //	myCache.ttl( "myKey" ) // will set ttl to default ttl

        //	myCache.ttl( "myKey", 1000 )

        this.ttl = this.ttl.bind(this);
        // ## getTtl

        // receive the ttl of a key.

        // **Parameters:**

        // * `key` ( String | Number ): cache key to check the ttl value

        // **Return**

        // ( Number|undefined ): The timestamp in ms when the key will expire, 0 if it will never expire or undefined if it not exists

        // **Example:**

        //	myCache.getTtl( "myKey" )

        this.getTtl = this.getTtl.bind(this);
        // ## keys

        // list all keys within this cache

        // **Return**

        // ( Array ): An array of all keys

        // **Example:**

        //     _keys = myCache.keys()

        //     # [ "foo", "bar", "fizz", "buzz", "anotherKeys" ]

        this.keys = this.keys.bind(this);
        // ## has

        // Check if a key is cached

        // **Parameters:**

        // * `key` ( String | Number ): cache key to check the ttl value

        // **Return**

        // ( Boolean ): A boolean that indicates if the key is cached

        // **Example:**

        //     _exists = myCache.has('myKey')

        //     # true

        this.has = this.has.bind(this);
        // ## getStats

        // get the stats

        // **Parameters:**

        // -

        // **Return**

        // ( Object ): Stats data

        // **Example:**

        //     myCache.getStats()
        //     # {
        //     # hits: 0,
        //     # misses: 0,
        //     # keys: 0,
        //     # ksize: 0,
        //     # vsize: 0
        //     # }

        this.getStats = this.getStats.bind(this);
        // ## flushAll

        // flush the whole data and reset the stats

        // **Example:**

        //     myCache.flushAll()

        //     myCache.getStats()
        //     # {
        //     # hits: 0,
        //     # misses: 0,
        //     # keys: 0,
        //     # ksize: 0,
        //     # vsize: 0
        //     # }

        this.flushAll = this.flushAll.bind(this);
        
        // ## flushStats

        // flush the stats and reset all counters to 0

        // **Example:**

        //     myCache.flushStats()

        //     myCache.getStats()
        //     # {
        //     # hits: 0,
        //     # misses: 0,
        //     # keys: 0,
        //     # ksize: 0,
        //     # vsize: 0
        //     # }

        this.flushStats = this.flushStats.bind(this);
        // ## close

        // This will clear the interval timeout which is set on checkperiod option.

        // **Example:**

        //     myCache.close()

        this.close = this.close.bind(this);
        // ## _checkData

        // internal housekeeping method.
        // Check all the cached data and delete the invalid values
        this._checkData = this._checkData.bind(this);
        // ## _check

        // internal method the check the value. If it's not valid any more delete it
        this._check = this._check.bind(this);
        // ## _isInvalidKey

        // internal method to check if the type of a key is either `number` or `string`
        this._isInvalidKey = this._isInvalidKey.bind(this);
        // ## _wrap

        // internal method to wrap a value in an object with some metadata
        this._wrap = this._wrap.bind(this);
        // ## _getValLength

        // internal method to calculate the value length
        this._getValLength = this._getValLength.bind(this);
        // ## _error

        // internal method to handle an error message
        this._error = this._error.bind(this);
        // ## _initErrors

        // internal method to generate error message templates
        this._initErrors = this._initErrors.bind(this);
        this.options = options;
        this._initErrors();
        // container for cached data
        this.data = {};
        // module options
        this.options = Object.assign({
          // convert all elements to string
          forceString: false,
          // used standard size for calculating value size
          objectValueSize: 80,
          promiseValueSize: 80,
          arrayValueSize: 40,
          // standard time to live in seconds. 0 = infinity;
          stdTTL: 0,
          // time in seconds to check all data and delete expired keys
          checkperiod: 600,
          // en/disable cloning of variables. If `true` you'll get a copy of the cached variable. If `false` you'll save and get just the reference
          useClones: true,
          // whether values should be deleted automatically at expiration
          deleteOnExpire: true,
          // enable legacy callbacks
          enableLegacyCallbacks: false,
          // max amount of keys that are being stored
          maxKeys: -1
        }, this.options);
        // generate functions with callbacks (legacy)
        if (this.options.enableLegacyCallbacks) {
          console.warn("WARNING! node-cache legacy callback support will drop in v6.x");
          ["get", "mget", "set", "del", "ttl", "getTtl", "keys", "has"].forEach((methodKey) => {
            var oldMethod;
            // reference real function
            oldMethod = this[methodKey];
            this[methodKey] = function(...args) {
              var cb, err, ref, res;
              ref = args, [...args] = ref, [cb] = splice.call(args, -1);
              // return a callback if cb is defined and a function
              if (typeof cb === "function") {
                try {
                  res = oldMethod(...args);
                  cb(null, res);
                } catch (error1) {
                  err = error1;
                  cb(err);
                }
              } else {
                return oldMethod(...args, cb);
              }
            };
          });
        }
        // statistics container
        this.stats = {
          hits: 0,
          misses: 0,
          keys: 0,
          ksize: 0,
          vsize: 0
        };
        // pre allocate valid keytypes array
        this.validKeyTypes = ["string", "number"];
        // initalize checking period
        this._checkData();
        return;
      }

      get(key) {
        var _ret, err;
        boundMethodCheck(this, NodeCache);
        // handle invalid key types
        if ((err = this._isInvalidKey(key)) != null) {
          throw err;
        }
        // get data and incremet stats
        if ((this.data[key] != null) && this._check(key, this.data[key])) {
          this.stats.hits++;
          _ret = this._unwrap(this.data[key]);
          // return data
          return _ret;
        } else {
          // if not found return undefined
          this.stats.misses++;
          return void 0;
        }
      }

      mget(keys) {
        var _err, err, i, key, len, oRet;
        boundMethodCheck(this, NodeCache);
        // convert a string to an array of one key
        if (!Array.isArray(keys)) {
          _err = this._error("EKEYSTYPE");
          throw _err;
        }
        // define return
        oRet = {};
        for (i = 0, len = keys.length; i < len; i++) {
          key = keys[i];
          // handle invalid key types
          if ((err = this._isInvalidKey(key)) != null) {
            throw err;
          }
          // get data and increment stats
          if ((this.data[key] != null) && this._check(key, this.data[key])) {
            this.stats.hits++;
            oRet[key] = this._unwrap(this.data[key]);
          } else {
            // if not found return a error
            this.stats.misses++;
          }
        }
        // return all found keys
        return oRet;
      }

      set(key, value, ttl) {
        var _err, err, existent;
        boundMethodCheck(this, NodeCache);
        // check if cache is overflowing
        if (this.options.maxKeys > -1 && this.stats.keys >= this.options.maxKeys) {
          _err = this._error("ECACHEFULL");
          throw _err;
        }
        // force the data to string
        if (this.options.forceString && !typeof value === "string") {
          value = JSON.stringify(value);
        }
        // set default ttl if not passed
        if (ttl == null) {
          ttl = this.options.stdTTL;
        }
        // handle invalid key types
        if ((err = this._isInvalidKey(key)) != null) {
          throw err;
        }
        // internal helper variables
        existent = false;
        // remove existing data from stats
        if (this.data[key]) {
          existent = true;
          this.stats.vsize -= this._getValLength(this._unwrap(this.data[key], false));
        }
        // set the value
        this.data[key] = this._wrap(value, ttl);
        this.stats.vsize += this._getValLength(value);
        // only add the keys and key-size if the key is new
        if (!existent) {
          this.stats.ksize += this._getKeyLength(key);
          this.stats.keys++;
        }
        this.emit("set", key, value);
        // return true
        return true;
      }

      mset(keyValueSet) {
        var _err, err, i, j, key, keyValuePair, len, len1, ttl, val;
        boundMethodCheck(this, NodeCache);
        // check if cache is overflowing
        if (this.options.maxKeys > -1 && this.stats.keys + keyValueSet.length >= this.options.maxKeys) {
          _err = this._error("ECACHEFULL");
          throw _err;
        }

// loop over keyValueSet to validate key and ttl
        for (i = 0, len = keyValueSet.length; i < len; i++) {
          keyValuePair = keyValueSet[i];
          ({key, val, ttl} = keyValuePair);
          // check if there is ttl and it's a number
          if (ttl && typeof ttl !== "number") {
            _err = this._error("ETTLTYPE");
            throw _err;
          }
          // handle invalid key types
          if ((err = this._isInvalidKey(key)) != null) {
            throw err;
          }
        }
        for (j = 0, len1 = keyValueSet.length; j < len1; j++) {
          keyValuePair = keyValueSet[j];
          ({key, val, ttl} = keyValuePair);
          this.set(key, val, ttl);
        }
        return true;
      }

      del(keys) {
        var delCount, err, i, key, len, oldVal;
        boundMethodCheck(this, NodeCache);
        // convert keys to an array of itself
        if (!Array.isArray(keys)) {
          keys = [keys];
        }
        delCount = 0;
        for (i = 0, len = keys.length; i < len; i++) {
          key = keys[i];
          // handle invalid key types
          if ((err = this._isInvalidKey(key)) != null) {
            throw err;
          }
          // only delete if existent
          if (this.data[key] != null) {
            // calc the stats
            this.stats.vsize -= this._getValLength(this._unwrap(this.data[key], false));
            this.stats.ksize -= this._getKeyLength(key);
            this.stats.keys--;
            delCount++;
            // delete the value
            oldVal = this.data[key];
            delete this.data[key];
            // return true
            this.emit("del", key, oldVal.v);
          }
        }
        return delCount;
      }

      take(key) {
        var _ret;
        boundMethodCheck(this, NodeCache);
        _ret = this.get(key);
        if ((_ret != null)) {
          this.del(key);
        }
        return _ret;
      }

      ttl(key, ttl) {
        var err;
        boundMethodCheck(this, NodeCache);
        ttl || (ttl = this.options.stdTTL);
        if (!key) {
          return false;
        }
        // handle invalid key types
        if ((err = this._isInvalidKey(key)) != null) {
          throw err;
        }
        // check for existent data and update the ttl value
        if ((this.data[key] != null) && this._check(key, this.data[key])) {
          // if ttl < 0 delete the key. otherwise reset the value
          if (ttl >= 0) {
            this.data[key] = this._wrap(this.data[key].v, ttl, false);
          } else {
            this.del(key);
          }
          return true;
        } else {
          // return false if key has not been found
          return false;
        }
      }

      getTtl(key) {
        var _ttl, err;
        boundMethodCheck(this, NodeCache);
        if (!key) {
          return void 0;
        }
        // handle invalid key types
        if ((err = this._isInvalidKey(key)) != null) {
          throw err;
        }
        // check for existant data and update the ttl value
        if ((this.data[key] != null) && this._check(key, this.data[key])) {
          _ttl = this.data[key].t;
          return _ttl;
        } else {
          // return undefined if key has not been found
          return void 0;
        }
      }

      keys() {
        var _keys;
        boundMethodCheck(this, NodeCache);
        _keys = Object.keys(this.data);
        return _keys;
      }

      has(key) {
        var _exists;
        boundMethodCheck(this, NodeCache);
        _exists = (this.data[key] != null) && this._check(key, this.data[key]);
        return _exists;
      }

      getStats() {
        boundMethodCheck(this, NodeCache);
        return this.stats;
      }

      flushAll(_startPeriod = true) {
        boundMethodCheck(this, NodeCache);
        // parameter just for testing

        // set data empty
        this.data = {};
        // reset stats
        this.stats = {
          hits: 0,
          misses: 0,
          keys: 0,
          ksize: 0,
          vsize: 0
        };
        // reset check period
        this._killCheckPeriod();
        this._checkData(_startPeriod);
        this.emit("flush");
      }

      flushStats() {
        boundMethodCheck(this, NodeCache);
        // reset stats
        this.stats = {
          hits: 0,
          misses: 0,
          keys: 0,
          ksize: 0,
          vsize: 0
        };
        this.emit("flush_stats");
      }

      close() {
        boundMethodCheck(this, NodeCache);
        this._killCheckPeriod();
      }

      _checkData(startPeriod = true) {
        var key, ref, value;
        boundMethodCheck(this, NodeCache);
        ref = this.data;
        // run the housekeeping method
        for (key in ref) {
          value = ref[key];
          this._check(key, value);
        }
        if (startPeriod && this.options.checkperiod > 0) {
          this.checkTimeout = setTimeout(this._checkData, this.options.checkperiod * 1000, startPeriod);
          if ((this.checkTimeout != null) && (this.checkTimeout.unref != null)) {
            this.checkTimeout.unref();
          }
        }
      }

      // ## _killCheckPeriod

      // stop the checkdata period. Only needed to abort the script in testing mode.
      _killCheckPeriod() {
        if (this.checkTimeout != null) {
          return clearTimeout(this.checkTimeout);
        }
      }

      _check(key, data) {
        var _retval;
        boundMethodCheck(this, NodeCache);
        _retval = true;
        // data is invalid if the ttl is too old and is not 0
        // console.log data.t < Date.now(), data.t, Date.now()
        if (data.t !== 0 && data.t < Date.now()) {
          if (this.options.deleteOnExpire) {
            _retval = false;
            this.del(key);
          }
          this.emit("expired", key, this._unwrap(data));
        }
        return _retval;
      }

      _isInvalidKey(key) {
        var ref;
        boundMethodCheck(this, NodeCache);
        if (ref = typeof key, indexOf.call(this.validKeyTypes, ref) < 0) {
          return this._error("EKEYTYPE", {
            type: typeof key
          });
        }
      }

      _wrap(value, ttl, asClone = true) {
        var livetime, now, oReturn, ttlMultiplicator;
        boundMethodCheck(this, NodeCache);
        if (!this.options.useClones) {
          asClone = false;
        }
        // define the time to live
        now = Date.now();
        livetime = 0;
        ttlMultiplicator = 1000;
        // use given ttl
        if (ttl === 0) {
          livetime = 0;
        } else if (ttl) {
          livetime = now + (ttl * ttlMultiplicator);
        } else {
          // use standard ttl
          if (this.options.stdTTL === 0) {
            livetime = this.options.stdTTL;
          } else {
            livetime = now + (this.options.stdTTL * ttlMultiplicator);
          }
        }
        // return the wrapped value
        return oReturn = {
          t: livetime,
          v: asClone ? clone(value) : value
        };
      }

      // ## _unwrap

      // internal method to extract get the value out of the wrapped value
      _unwrap(value, asClone = true) {
        if (!this.options.useClones) {
          asClone = false;
        }
        if (value.v != null) {
          if (asClone) {
            return clone(value.v);
          } else {
            return value.v;
          }
        }
        return null;
      }

      // ## _getKeyLength

      // internal method the calculate the key length
      _getKeyLength(key) {
        return key.toString().length;
      }

      _getValLength(value) {
        boundMethodCheck(this, NodeCache);
        if (typeof value === "string") {
          // if the value is a String get the real length
          return value.length;
        } else if (this.options.forceString) {
          // force string if it's defined and not passed
          return JSON.stringify(value).length;
        } else if (Array.isArray(value)) {
          // if the data is an Array multiply each element with a defined default length
          return this.options.arrayValueSize * value.length;
        } else if (typeof value === "number") {
          return 8;
        } else if (typeof (value != null ? value.then : void 0) === "function") {
          // if the data is a Promise, use defined default
          // (can't calculate actual/resolved value size synchronously)
          return this.options.promiseValueSize;
        } else if (typeof Buffer !== "undefined" && Buffer !== null ? Buffer.isBuffer(value) : void 0) {
          return value.length;
        } else if ((value != null) && typeof value === "object") {
          // if the data is an Object multiply each element with a defined default length
          return this.options.objectValueSize * Object.keys(value).length;
        } else if (typeof value === "boolean") {
          return 8;
        } else {
          // default fallback
          return 0;
        }
      }

      _error(type, data = {}) {
        var error;
        boundMethodCheck(this, NodeCache);
        // generate the error object
        error = new Error();
        error.name = type;
        error.errorcode = type;
        error.message = this.ERRORS[type] != null ? this.ERRORS[type](data) : "-";
        error.data = data;
        // return the error object
        return error;
      }

      _initErrors() {
        var _errMsg, _errT, ref;
        boundMethodCheck(this, NodeCache);
        this.ERRORS = {};
        ref = this._ERRORS;
        for (_errT in ref) {
          _errMsg = ref[_errT];
          this.ERRORS[_errT] = this.createErrorMessage(_errMsg);
        }
      }

      createErrorMessage(errMsg) {
        return function(args) {
          return errMsg.replace("__key", args.type);
        };
      }

    };

    NodeCache.prototype._ERRORS = {
      "ENOTFOUND": "Key `__key` not found",
      "ECACHEFULL": "Cache max keys amount exceeded",
      "EKEYTYPE": "The key argument has to be of type `string` or `number`. Found: `__key`",
      "EKEYSTYPE": "The keys argument has to be an array.",
      "ETTLTYPE": "The ttl argument has to be a number."
    };

    return NodeCache;

  }).call(this);

}).call(this);
