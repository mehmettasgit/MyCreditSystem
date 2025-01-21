package com.creditsystem.service;

import com.creditsystem.entity.RoleType;
import com.creditsystem.entity.User;
import com.creditsystem.entity.UserRole;
import com.creditsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String username, String password, String roleName) {

        RoleType parsedRole = RoleType.valueOf(roleName);

        UserRole userRole = userRoleService.findRoleByName(parsedRole);

        User user = new User();
        user.setUsername(username);

        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRole(userRole);

        return userRepository.save(user);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id" + id));
    }
}
