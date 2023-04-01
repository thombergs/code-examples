package com.reflectoring.userdetails.web;

import com.fasterxml.jackson.annotation.JsonView;
import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.persistence.Views;
import com.reflectoring.userdetails.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
@RestController
@RequestMapping("/external")
public class ExternalUserController {
    private static final Logger log = LoggerFactory.getLogger(ExternalUserController.class);

    private final UserService userService;

    public ExternalUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userdetails/all")
    @JsonView(Views.UserDetailedSummary.class)
    public ResponseEntity<UserData> getExtDetailUsers(@RequestParam String loginId) {
        return ResponseEntity.ok().body(userService.getUser(loginId, false));
    }

    @GetMapping("/userdetails")
    @JsonView(Views.UserSummary.class)
    public ResponseEntity<UserData> getExtUserSummary(@RequestParam String loginId) {
        return ResponseEntity.ok().body(userService.getUser(loginId, false));
    }

    @GetMapping("/users")
    @JsonView(Views.ExternalView.class)
    public ResponseEntity<List<UserData>> getExtUsers(@RequestParam(required = false) String loginId) {
        if (Objects.isNull(loginId)) {
            return ResponseEntity.ok().body(userService.getAllUsers(false));
        } else {
            return ResponseEntity.ok().body(List.of(userService.getUser(loginId, false)));
        }

    }

    @PatchMapping("/users")
    public ResponseEntity<UserData> updateExtUserAddress(@RequestParam String loginId, @RequestBody @JsonView(Views.PatchView.class) UserData addressData) {
        return ResponseEntity.ok().body(userService.updateAddress(loginId, addressData, false));
    }

}
