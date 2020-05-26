package io.reflectoring.cache.cleint.client;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import io.reflectoring.cache.cleint.rest.Car;
import org.springframework.stereotype.Component;

@Component
public class CacheClient {

    private static final String CARS = "cars";

    private HazelcastInstance client = HazelcastClient.newHazelcastClient();

    public Car put(String key, Car car){
        IMap<Object, Object> map = client.getMap(CARS);
        return (Car) map.put(key, car);
    }

    public Car get(String key){
        IMap<Object, Object> map = client.getMap(CARS);
        return (Car) map.get(key);
    }
}
