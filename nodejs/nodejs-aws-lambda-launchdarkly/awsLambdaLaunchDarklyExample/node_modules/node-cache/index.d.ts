// Type definitions for node-cache 5
// Project: https://github.com/tcs-de/nodecache
// Definitions by: Ilya Mochalov <https://github.com/chrootsu>
//                 Daniel Thunell <https://github.com/dthunell>
//                 Ulf Seltmann <https://github.com/useltmann>
// Definitions: https://github.com/DefinitelyTyped/DefinitelyTyped

/// <reference types="node" />

declare namespace NodeCache {
	interface NodeCacheLegacyCallbacks {
		/** container for cached data */
		data: Data;

		/** module options */
		options: Options;

		/** statistics container */
		stats: Stats;

		/**
		 * get a cached key and change the stats
		 *
		 * @param key cache key or an array of keys
		 * @param cb Callback function
		 */
		get<T>(
			key: Key,
			cb?: Callback<T>
		): T | undefined;

		/**
		 * get multiple cached keys at once and change the stats
		 *
		 * @param keys an array of keys
		 * @param cb Callback function
		 */
		mget<T>(
			keys: Key[],
			cb?: Callback<{ [key: string]: T }>
		): { [key: string]: T };

		/**
		 * set a cached key and change the stats
		 *
		 * @param key cache key
		 * @param value A element to cache. If the option `option.forceString` is `true` the module trys to translate
		 * it to a serialized JSON
		 * @param ttl The time to live in seconds.
		 * @param cb Callback function
		 */
		set<T>(
			key: Key,
			value: T,
			ttl: number | string,
			cb?: Callback<boolean>
		): boolean;

		set<T>(
			key: Key,
			value: T,
			cb?: Callback<boolean>
		): boolean;

		/**
		 * set multiple cached keys at once and change the stats
		 *
		 * @param keyValueSet  an array of object which includes key,value and ttl
		 */
		mset<T>(
			keyValueSet: ValueSetItem<T>[],
		): boolean;

		/**
		 * remove keys
		 * @param keys cache key to delete or a array of cache keys
		 * @param cb Callback function
		 * @returns Number of deleted keys
		 */
		del(
			keys: Key | Key[],
			cb?: Callback<number>
		): number;

		/**
		 * reset or redefine the ttl of a key. If `ttl` is not passed or set to 0 it's similar to `.del()`
		 */
		ttl(
			key: Key,
			ttl: number,
			cb?: Callback<boolean>
		): boolean;

		ttl(
			key: Key,
			cb?: Callback<boolean>
		): boolean;

		getTtl(
			key: Key,
		): number|undefined;

		getTtl(
			key: Key,
			cb?: Callback<boolean>
		): boolean;

		/**
		 * list all keys within this cache
		 * @param cb Callback function
		 * @returns An array of all keys
		 */
		keys(cb?: Callback<string[]>): string[];

		/**
		 * get the stats
		 *
		 * @returns Stats data
		 */
		getStats(): Stats;

		/**
		 * flush the whole data and reset the stats
		 */
		flushAll(): void;

		/**
		 * This will clear the interval timeout which is set on checkperiod option.
		 */
		close(): void;
	}

	/**
	 * Since 4.1.0: Key-validation: The keys can be given as either string or number,
	 * but are casted to a string internally anyway.
	 */
	type Key = string | number;

	type ValueSetItem<T = any> = {
		key: Key;
		val: T;
		ttl?: number;
	}

	interface Data {
		[key: string]: WrappedValue<any>;
	}

	interface Options {
		/**
		 * If enabled, all values will be stringified during the set operation
		 *
		 * @type {boolean}
		 * @memberof Options
		 */
		forceString?: boolean;

		objectValueSize?: number;
		promiseValueSize?: number;
		arrayValueSize?: number;

		/**
		 * standard time to live in seconds. 0 = infinity
		 *
		 * @type {number}
		 * @memberof Options
		 */
		stdTTL?: number;

		/**
		 * time in seconds to check all data and delete expired keys
		 *
		 * @type {number}
		 * @memberof Options
		 */
		checkperiod?: number;

		/**
		 * en/disable cloning of variables.
		 * disabling this is strongly encouraged when aiming for performance!
		 *
		 * If `true`: set operations store a clone of the value and get operations will create a fresh clone of the cached value
		 * If `false` you'll just store a reference to your value
		 *
		 * @type {boolean}
		 * @memberof Options
		 */
		useClones?: boolean;

		errorOnMissing?: boolean;
		deleteOnExpire?: boolean;

		/**
		 * enable legacy callbacks.
		 * legacy callback support will drop in v6.x!
		 *
		 * @type {boolean}
		 * @memberof Options
		 */
		enableLegacyCallbacks?: boolean;

		/**
		 * max amount of keys that are being stored.
		 * set operations will throw an error when the cache is full
		 *
		 * @type {number}
		 * @memberof Options
		 */
		maxKeys?: number;
	}

	interface Stats {
		hits: number;
		misses: number;
		keys: number;
		ksize: number;
		vsize: number;
	}

	interface WrappedValue<T> {
		// ttl
		t: number;
		// value
		v: T;
	}

	type Callback<T> = (err: any, data: T | undefined) => void;
}

import events = require("events");

import Data = NodeCache.Data;
import Key = NodeCache.Key;
import Options = NodeCache.Options;
import Stats = NodeCache.Stats;
import Callback = NodeCache.Callback;
import ValueSetItem = NodeCache.ValueSetItem;
import NodeCacheLegacyCallbacks = NodeCache.NodeCacheLegacyCallbacks;

declare class NodeCache extends events.EventEmitter {
	/** container for cached data */
	data: Data;

	/** module options */
	options: Options;

	/** statistics container */
	stats: Stats;

	/** constructor */
	constructor(options?: Options);

	/**
	 * get a cached key and change the stats
	 *
	 * @param key cache key
	 * @returns The value stored in the key
	 */
	get<T>(
		key: Key
	): T | undefined;

	/**
	 * get multiple cached keys at once and change the stats
	 *
	 * @param keys an array of keys
	 * @returns an object containing the values stored in the matching keys
	 */
	mget<T>(
		keys: Key[]
	): { [key: string]: T };

	/**
	 * set a cached key and change the stats
	 *
	 * @param key cache key
	 * @param value A element to cache. If the option `option.forceString` is `true` the module trys to translate
	 * it to a serialized JSON
	 * @param ttl The time to live in seconds.
	 */
	set<T>(
		key: Key,
		value: T,
		ttl: number | string
	): boolean;

	set<T>(
		key: Key,
		value: T
	): boolean;

	/**
	 * set multiple cached keys at once and change the stats
	 *
	 * @param keyValueSet an array of object which includes key,value and ttl
	 */
	mset<T>(
		keyValueSet: ValueSetItem<T>[]
	): boolean;

	/**
	 * remove keys
	 * @param keys cache key to delete or a array of cache keys
	 * @param cb Callback function
	 * @returns Number of deleted keys
	 */
	del(
		keys: Key | Key[]
	): number;

	/**
	 * get a cached key and remove it from the cache.
	 * Equivalent to calling `get(key)` + `del(key)`.
	 * Useful for implementing `single use` mechanism such as OTP, where once a value is read it will become obsolete.
	 *
	 * @param key cache key
	 * @returns The value stored in the key
	 */
	take<T>(
		key: Key
	): T | undefined;
					  
	/**
	 * reset or redefine the ttl of a key. If `ttl` is not passed or set to 0 it's similar to `.del()`
	 */
	ttl(
		key: Key,
		ttl: number
	): boolean;

	ttl(
		key: Key
	): boolean;

	getTtl(
		key: Key,
	): number|undefined;

	getTtl(
		key: Key
	): boolean;

	/**
	 * list all keys within this cache
	 * @returns An array of all keys
	 */
	keys(): string[];

	/**
	 * get the stats
	 *
	 * @returns Stats data
	 */
	getStats(): Stats;

	/**
	 * Check if a key is cached
	 * @param key cache key to check
	 * @returns Boolean indicating if the key is cached or not
	 */
	has(key: Key): boolean;

	/**
	 * flush the whole data and reset the stats
	 */
	flushAll(): void;

	/**
	 * This will clear the interval timeout which is set on checkperiod option.
	 */
	close(): void;

	/**
	 * flush the stats and reset all counters to 0
	 */
	flushStats(): void;
}


export = NodeCache;
