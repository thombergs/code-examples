package io.reflectoring.javaconfig.controllers;

import io.reflectoring.javaconfig.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    @Qualifier(value = "core")
    private Department core;

    @GetMapping(value = "{id}")
    public Department getDepartmentById(@PathVariable("id") final Long id) {
        return core;
    }
}
