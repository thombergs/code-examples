package io.reflectoring.javaconfig;

//import net.sf.ehcache.Cache;
//import net.sf.ehcache.CacheManager;
//import net.sf.ehcache.config.CacheConfiguration;
//import net.sf.ehcache.config.PersistenceConfiguration;
//import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class AppCacheConfiguration {

    @Bean(name = "employees-cache")
    public CacheManager employeesCacheManager() {
        final CacheManager manager = CacheManager.create();

        //Create a Cache specifying its configuration.
        final Cache employeesCache = new Cache(
            new CacheConfiguration("employeesCache", 1000)
                .memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU)
                .eternal(false)
                .timeToLiveSeconds(60)
                .timeToIdleSeconds(30)
                .diskExpiryThreadIntervalSeconds(0)
                .persistence(new PersistenceConfiguration().strategy(PersistenceConfiguration.Strategy.LOCALTEMPSWAP)));
        manager.addCache(employeesCache);
        return manager;
    }
}
