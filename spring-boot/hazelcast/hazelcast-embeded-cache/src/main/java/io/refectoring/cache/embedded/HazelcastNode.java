package io.refectoring.cache.embedded;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import io.refectoring.cache.embedded.rest.Car;
import org.springframework.stereotype.Component;

@Component
public class HazelcastNode {

    public static final String CARS = "cars";
    private final HazelcastInstance hzInstance = Hazelcast.newHazelcastInstance(createConfig());

    public String put(String number, Car car){
        IMap<Object, Object> map = hzInstance.getMap(CARS);
        return (String) map.put(number, car);
    }

    public Car get(String key){
        IMap<Object, Object> map = hzInstance.getMap(CARS);
        return (Car) map.get(key);
    }

    public Config createConfig() {
        Config config = new Config();
        MapConfig mapConfig = new MapConfig(CARS);
        mapConfig.setTimeToLiveSeconds(20);
        mapConfig.setMaxIdleSeconds(360);
        config.addMapConfig(mapConfig);
        return config;
    }
}
