package io.reflectoring.javaconfig.controllers;

import io.reflectoring.javaconfig.model.Organization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/organizations")
public class OrganizationController {

    @Autowired
    @Qualifier(value = "acme")
    private Organization acme;

    @GetMapping(value = "{id}", produces = APPLICATION_JSON_VALUE)
    public Organization getOrganizationById(@PathVariable("id") final Long id) {
        return acme;
    }
}
