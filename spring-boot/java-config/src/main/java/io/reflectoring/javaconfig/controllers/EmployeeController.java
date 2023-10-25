package io.reflectoring.javaconfig.controllers;

import io.reflectoring.javaconfig.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    @Qualifier(value = "founder")
    private Employee founder;

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public Employee getEmployeeById(@PathVariable("id") final Long id) {
        return founder;
    }
}
