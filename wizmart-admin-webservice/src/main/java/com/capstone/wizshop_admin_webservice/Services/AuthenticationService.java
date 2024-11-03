// src/main/java/com/capstone/wizshop_admin_webservice/services/AuthenticationService.java

package com.capstone.wizshop_admin_webservice.Services;

import com.capstone.wizshop_admin_webservice.controller.AdminController;
import com.capstone.wizshop_admin_webservice.model.User;
import com.capstone.wizshop_admin_webservice.repositories.UserRepository;
import com.capstone.wizshop_admin_webservice.properties.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class AuthenticationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordService passwordService;

    private TokenService tokenService;

    @Autowired
    private Properties properties;

    @PostConstruct
    public void init() {
        this.tokenService = new TokenService(properties.getJwtSecret());
    }

    public String authenticate(String username, String rawPassword) {
    	logger.info("authentication started");
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("Invalid credentials");
        }

        if (!passwordService.matches(rawPassword, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        return tokenService.generateToken(user.getUsername()); 
    }

}
