package io.reflectoring.publisher_register.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document(collection = "Publisher")
public class Publisher {
    @Id
    private String id;
    private String name;
    private String email;
    private Integer published;
}
