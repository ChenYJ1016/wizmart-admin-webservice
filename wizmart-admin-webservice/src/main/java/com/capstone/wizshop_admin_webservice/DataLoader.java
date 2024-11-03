// src/main/java/com/capstone/wizshop_admin_webservice/DataLoader.java

package com.capstone.wizshop_admin_webservice;

import com.capstone.wizshop_admin_webservice.model.User;
import com.capstone.wizshop_admin_webservice.repositories.UserRepository;
import com.capstone.wizshop_admin_webservice.Services.PasswordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    @Override
    public void run(String... args) throws Exception {
        // Check if user already exists
        if (userRepository.findByUsername("admin") == null) {
            User user = new User();
            user.setUsername("admin");
            user.setPasswordHash(passwordService.hashPassword("admin123")); // Replace with your desired password
            user.setRoles(Collections.singleton("ADMIN"));
            userRepository.save(user);
        }
    }
}
