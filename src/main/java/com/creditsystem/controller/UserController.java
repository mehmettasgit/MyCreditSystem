package com.creditsystem.controller;


import com.creditsystem.entity.User;
import com.creditsystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    static class CreateUserRequest {

        public String username;
        public String password;
        public String roleName;
    }

    @PostMapping("/create")
    public User create(@RequestBody CreateUserRequest request) {
        return userService.createUser(
                request.username,
                request.password,
                request.roleName
        );

    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
}
