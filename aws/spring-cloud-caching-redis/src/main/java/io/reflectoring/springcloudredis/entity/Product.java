package io.reflectoring.springcloudredis.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Product implements Serializable {
    @Id
    private String id;

    private String name;

    private Double price;

    private LocalDateTime manufacturingDate;

    @Transient
    private Object data;

    private Double weight;

    @Embedded
    private Dimension dimension;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Embeddable
    @Getter
    @Setter
    public static class Dimension implements Serializable {
        private Double height;

        private Double width;
    }


}
