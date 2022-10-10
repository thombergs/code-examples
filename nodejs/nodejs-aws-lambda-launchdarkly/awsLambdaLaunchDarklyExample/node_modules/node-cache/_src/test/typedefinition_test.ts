import NodeCache = require("../../");

import Options = NodeCache.Options;
import Stats = NodeCache.Stats;
import Callback = NodeCache.Callback;

interface TypeSample {
	a: number;
	b: string;
	c: boolean;
}

{
	let options: Options;
	let cache: NodeCache;
	cache = new NodeCache();
	cache = new NodeCache(options);
}

{
	let cache: NodeCache;
	let key: string;
	let result: TypeSample | undefined;
	result = cache.get<TypeSample>(key);
	result = cache.get<TypeSample>(key);
}

{
	let cache: NodeCache;
	let keys: string[];
	let result: { [key: string]: TypeSample };
	result = cache.mget<TypeSample>(keys);
	result = cache.mget<TypeSample>(keys);
}

{
	let cache: NodeCache;
	let key: string;
	let value: TypeSample;
	let ttl: number | string;
	let result: boolean;
	result = cache.set<TypeSample>(key, value);
	result = cache.set<TypeSample>(key, value, ttl);
	result = cache.set<TypeSample>(key, value, ttl);
	result = cache.set<TypeSample>(key, value);
}

{
	let cache: NodeCache;
	let keys: string | string[];
	let result: number;
	result = cache.del(keys);
	result = cache.del(keys);
}

{
	let cache: NodeCache;
	let key: string;
	let ttl: number;
	let result: boolean;
	result = cache.ttl(key);
	result = cache.ttl(key, ttl);
	result = cache.ttl(key, ttl);
	result = cache.ttl(key);
}

{
	let cache: NodeCache;
	let result: string[];
	result = cache.keys();
	result = cache.keys();
}

{
	let cache: NodeCache;
	let key: string | number;
	let result: boolean;
	result = cache.has(key);
	result = cache.has(key);
}

{
	let cache: NodeCache;
	let result: Stats;
	result = cache.getStats();
}

{
	let cache: NodeCache;
	let key: string;
	let number: number;
	let result1: number | undefined;
	result1 = cache.getTtl(key);
}

/* tslint:disable void-return no-void-expression */
{
	let cache: NodeCache;
	let result: void;
	result = cache.flushAll();
}

{
	let cache: NodeCache;
	let result: void;
	result = cache.close();
}
/* tslint:enable void-return */
