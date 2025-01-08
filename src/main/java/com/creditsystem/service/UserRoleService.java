package com.creditsystem.service;


import com.creditsystem.entity.RoleType;
import com.creditsystem.entity.UserRole;
import com.creditsystem.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    public UserRole createRole(UserRole userRole){

        return  userRoleRepository.save(userRole);
    }

    public UserRole findRoleById(Long id){
        return userRoleRepository.findById(id).orElseThrow(()-> new RuntimeException("Role nor found"));
    }

   //Findbyrolename should be arranged.

}
