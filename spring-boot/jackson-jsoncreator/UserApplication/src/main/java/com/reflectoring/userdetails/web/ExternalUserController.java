package com.reflectoring.userdetails.web;

import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/external")
public class ExternalUserController {
    private static final Logger log = LoggerFactory.getLogger(ExternalUserController.class);

    private final UserService userService;

    public ExternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userdetails/all")
    public ResponseEntity<List<UserData>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

}
