package com.creditsystem.controller;


import com.creditsystem.DTO.JwtAuthResponse;
import com.creditsystem.components.JwtTokenProvider;
import com.creditsystem.entity.User;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user ){

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails)  authentication.getPrincipal();
        String roleName = userDetails.getAuthorities().iterator().next().getAuthority();

        long expirationForAdmin = 24*60*60*1000;
        long expiratşonForOthers = 7*24*60*60*1000;

        String jwt = jwtTokenProvider.generateToken(
                userDetails.getUsername(),
                roleName,
                roleName.equals("ROLE_ADMIN") ? expirationForAdmin :expiratşonForOthers
        );

        return ResponseEntity.ok(new JwtAuthResponse(jwt));
    }

}
