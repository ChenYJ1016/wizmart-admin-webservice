package com.capstone.wizmart_admin_webservice.controller;


import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Controller
public class HomeController {
	
	private static final SecureRandom secureRandom = new SecureRandom(); 
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    @Value("${aws.cognito.logoutUrl}")
    private String cognitoLogoutUrl;

    @Value("${aws.cognito.logout.success.redirectUrl}")
    private String logoutSuccessRedirectUrl;

    @GetMapping("/")
    public String publicPage() {
        return "public"; 
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin"; 
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();

            // Generate state
            String state = generateState();

            String logoutUrl = cognitoLogoutUrl + "?response_type=code" 
                    + "&client_id=" + oidcUser.getAudience().get(0) 
                    + "&redirect_uri=" + logoutSuccessRedirectUrl 
                    + "&state=" + state 
                    + "&nonce=" + oidcUser.getNonce();

            new SecurityContextLogoutHandler().logout(request, response, authentication);
            return "redirect:" + logoutUrl;
        }
        return "redirect:/";
    }

    private String generateState() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }
}
