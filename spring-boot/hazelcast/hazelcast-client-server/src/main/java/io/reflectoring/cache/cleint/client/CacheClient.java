package io.reflectoring.cache.cleint.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.reflectoring.cache.cleint.rest.Car;
import org.springframework.stereotype.Component;

@Component
public class CacheClient {

    private static final String CARS = "cars";

    private HazelcastInstance client = HazelcastClient.newHazelcastClient();

    public Car put(String key, Car car){
        IMap<String, Car> map = client.getMap(CARS);
        return map.putIfAbsent(key, car);
    }

    public Car get(String key){
        IMap<String, Car> map = client.getMap(CARS);
        return map.get(key);
    }
}
