package com.capstone.wizmart_admin_webservice.controller;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.capstone.wizmart_admin_webservice.handlers.ProductCommandHandler;
import com.capstone.wizmart_admin_webservice.model.Products;
import com.capstone.wizmart_admin_webservice.model.commands.CreateProductCommand;
import com.capstone.wizmart_admin_webservice.model.commands.DeleteProductCommand;
import com.capstone.wizmart_admin_webservice.model.commands.UpdateProductCommand;
import com.capstone.wizmart_admin_webservice.properties.Properties;
import com.capstone.wizmart_admin_webservice.services.ProductCommandService;
import com.capstone.wizmart_admin_webservice.services.ProductQueryService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin")
public class HomeController {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private Properties properties;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private ProductCommandService productCommandService;
    
    @Autowired
    private ProductCommandHandler productCommandHandler;

    @GetMapping
    public String redirectToAdmin() {
        try {
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in redirectToAdmin", e);
            return "error"; // Return an error page
        }
    }

    @GetMapping("/")
    public String adminPage(Model model) {
        try {
            List<Products> products = productQueryService.getAllProducts();
            model.addAttribute("products", products);
            return "admin";
        } catch (Exception e) {
            logger.error("Error in adminPage", e);
            return "error"; // Return an error page
        }
    }

    @PostMapping("/products/create")
    public String createProduct(@ModelAttribute CreateProductCommand command) {
        try {
        	productCommandService.createProduct(command);
        	return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in createProduct", e);
            return "error"; // Return an error page
        }
    }

    @PutMapping("/products/update/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @ModelAttribute UpdateProductCommand command) {
        try {

            command.setProductId(productId);
            productCommandService.updateProduct(command);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in updateProduct", e);
            return "error"; // Return an error page
        }
    }

    @DeleteMapping("/products/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        try {

            DeleteProductCommand command = new DeleteProductCommand();
            command.setProductId(productId);
            productCommandService.deleteProduct(command);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in deleteProduct", e);
            return "error"; // Return an error page
        }
    }


    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                String cognitoLogoutUrl = properties.getLogoutUrl();
                String logoutSuccessRedirectUrl = properties.getLogoutSuccessRedirectUrl();
                OidcUser oidcUser = (OidcUser) oauthToken.getPrincipal();

                String state = generateState();

                String logoutUrl = cognitoLogoutUrl + "?response_type=code"
                        + "&client_id=" + oidcUser.getAudience().get(0)
                        + "&redirect_uri=" + logoutSuccessRedirectUrl
                        + "&state=" + state
                        + "&nonce=" + oidcUser.getNonce();

                new SecurityContextLogoutHandler().logout(request, response, authentication);
                return "redirect:" + logoutUrl;
            }
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in logout", e);
            return "error"; // Return an error page
        }
    }

    private String generateState() {
        try {
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            return base64Encoder.encodeToString(randomBytes);
        } catch (Exception e) {
            logger.error("Error in generateState", e);
            return null; // Return a default or null value
        }
    }
}
