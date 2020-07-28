package io.refectoring.cache.embedded.serializer;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import io.refectoring.cache.embedded.rest.Car;

import java.io.IOException;

public class CarSerializer implements StreamSerializer<Car> {

    @Override
    public void write(ObjectDataOutput out, Car object) throws IOException {
       out.writeUTF(object.getName());
       out.writeUTF(object.getNumber());
    }

    @Override
    public Car read(ObjectDataInput in) throws IOException {
        return Car.builder()
                .name(in.readUTF())
                .number(in.readUTF())
                .build();
    }

    @Override
    public int getTypeId() {
        return 1;
    }
}
