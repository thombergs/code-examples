package com.reflectoring.csrf.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping
    public String attackerPage() {
        return "attacker";
    }
}
