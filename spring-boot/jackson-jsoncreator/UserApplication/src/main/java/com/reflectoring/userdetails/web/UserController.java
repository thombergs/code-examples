package com.reflectoring.userdetails.web;

import com.reflectoring.userdetails.model.UserData;
import com.reflectoring.userdetails.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/data")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userdetails/all")
    public ResponseEntity<List<UserData>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/userdetails/page")
    public ResponseEntity<Page<UserData>> getPagedUser(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "20") int size) {
        List<UserData> usersList = userService.getUsers();

        // First let's split the List depending on the pagesize
        int totalCount = usersList.size();
        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalCount);

        List<UserData> pageContent = usersList.subList(startIndex, endIndex);

        Page<UserData> employeeDtos = new PageImpl<>(pageContent, PageRequest.of(page, size), totalCount);

        return ResponseEntity.ok()
                .body(employeeDtos);
    }

}
