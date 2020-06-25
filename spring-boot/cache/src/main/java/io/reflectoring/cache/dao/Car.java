package io.reflectoring.cache.dao;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.IOException;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car implements DataSerializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String color;

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        out.writeUTF(id.toString());
        out.writeUTF(name);
        out.writeUTF(color);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        id = UUID.fromString(in.readUTF());
        name = in.readUTF();
        color = in.readUTF();
    }
}
