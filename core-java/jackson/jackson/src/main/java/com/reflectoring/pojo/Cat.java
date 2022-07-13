package com.reflectoring.pojo;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
public class Cat {
    private String name;

    @JsonAnyGetter
    Map<String, String> map = Map.of(
            "name", "Jack",
            "surname", "wolfskin"
    );

    @JsonGetter("catName")
    public String getName() {
        return name;
    }

    public Cat(String name) {
        this.name = name;
    }
}
