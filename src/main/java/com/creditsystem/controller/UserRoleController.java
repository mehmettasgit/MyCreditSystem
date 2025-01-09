package com.creditsystem.controller;

import com.creditsystem.entity.UserRole;
import com.creditsystem.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping("/create")
    public UserRole create(@RequestBody UserRole userRole) {
        return userRoleService.createRole(userRole);
    }

    @GetMapping("/{id}")
    public UserRole getById(@PathVariable Long id) {
        return userRoleService.findRoleById(id);
    }
}
