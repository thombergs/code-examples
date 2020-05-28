package io.refectoring.cache.embedded;

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.refectoring.cache.embedded.rest.Car;
import org.springframework.stereotype.Component;

@Component
public class HazelcastNode {

    public static final String CARS = "cars";
    private final HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance(createConfig());

    public Car put(String number, Car car){
        IMap<String, Car> map = hzInstance.getMap(CARS);
        return map.putIfAbsent(number, car);
    }

    public Car get(String key){
        IMap<String, Car> map = hzInstance.getMap(CARS);
        return map.get(key);
    }

    public Config createConfig() {
        Config config = new Config();
        config.addMapConfig(mapConfig());
        return config;
    }

    private MapConfig mapConfig() {
        MapConfig mapConfig = new MapConfig(CARS);
        mapConfig.setTimeToLiveSeconds(20);
        mapConfig.setMaxIdleSeconds(360);
        return mapConfig;
    }
}
