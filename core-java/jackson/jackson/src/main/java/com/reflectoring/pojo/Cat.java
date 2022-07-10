package com.reflectoring.pojo;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Cat {
    private String name;

    @JsonGetter("catName")
    public String getName() {
        return name;
    }
}
