package io.reflectoring.specification.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id
    private String id;

    private String name;

    private Double price;

    private LocalDateTime manufacturingDate;

    @ManyToOne
    private Address manufacturingPlace;

    private Double weight;

    @Embedded
    private Dimension dimension;

    @ManyToOne
    private Distributor distributor;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Embeddable
    @Getter
    @Setter
    public static class Dimension {
        private Double height;

        private Double width;
    }


}
