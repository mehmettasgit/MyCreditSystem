package com.creditsystem.config;


import com.creditsystem.components.JwtAuthenticationFilter;
import com.creditsystem.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurtiyConfig {


    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return ((org.springframework.security.config.annotation.web.builders.HttpSecurity) http)
                .csrf(csrf -> csrf.disable()) // CSRF'yi devre dışı bırakıyoruz (REST için genelde)
                .authorizeHttpRequests(auth -> auth
                        /*.antMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/users/create").permitAll()
                        .antMatchers(HttpMethod.GET, "/api/**").hasAnyRole("ADMIN", "USER", "MODERATOR")
                        .antMatchers(HttpMethod.POST, "/api/**").hasAnyRole("ADMIN", "USER")
                        .antMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                        .antMatchers("/api/auth/**").permitAll() // login, register herkese açık
                        .anyRequest().authenticated()*/
                        .antMatchers("/actuator/**").permitAll()
                        .anyRequest().permitAll()

                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Filtreyi ekle
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public org.springframework.security.authentication.AuthenticationManager
    authenticationManager(org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }
}
