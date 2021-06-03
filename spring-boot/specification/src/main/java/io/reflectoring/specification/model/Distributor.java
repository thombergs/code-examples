package io.reflectoring.specification.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
public class Distributor {
    @Id
    private String id;

    private String name;

    @OneToOne
    private Address address;

}
