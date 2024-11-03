// src/main/java/com/capstone/wizshop_admin_webservice/controller/LoginController.java

package com.capstone.wizshop_admin_webservice.controller;

import com.capstone.wizshop_admin_webservice.Services.AuthenticationService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/auth")
public class LoginController {
	
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam String username,
            @RequestParam String rawPassword,
            HttpServletRequest request,
            Model model) {
        try {
            logger.info("Attempting to log in");

            // Sanitize inputs to prevent XSS
            String sanitizedUsername = HtmlUtils.htmlEscape(username.trim());
            String sanitizedPassword = HtmlUtils.htmlEscape(rawPassword.trim());

            String token = authenticationService.authenticate(sanitizedUsername, sanitizedPassword);
            
            // Store token in session
            HttpSession session = request.getSession();
            session.setAttribute("token", token);

            return "redirect:/admin/";
        } catch (Exception e) {
            model.addAttribute("error", "Invalid credentials");
            return "login";
        }
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate session
        HttpSession session = request.getSession(false);  // Get current session, if it exists
        if (session != null) {
            session.invalidate(); // Invalidate the session to clear any stored data
        }
        logger.info("User logged out successfully");
        return "redirect:/auth/login"; // Redirect to login page after logout
    }
}
