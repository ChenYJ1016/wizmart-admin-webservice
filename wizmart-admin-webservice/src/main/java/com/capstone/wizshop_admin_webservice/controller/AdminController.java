// src/main/java/com/capstone/wizshop_admin_webservice/controller/AdminController.java

package com.capstone.wizshop_admin_webservice.controller;

import com.capstone.wizshop_admin_webservice.DTO.*;
import com.capstone.wizshop_admin_webservice.properties.Properties;
import com.capstone.wizshop_admin_webservice.Services.AdminS3Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private Properties properties;

    @Autowired
    private AdminS3Service s3Service;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String viewAdminPage(Model model, HttpServletRequest request) {
        try {
            logger.info("view admin");

            // Get token from session and log it for debugging
            String token = (String) request.getSession().getAttribute("token");
            if (token == null) {
                logger.warn("Token is missing, redirecting to login");
                return "redirect:/auth/login";
            }

            logger.info("Token found: " + token);

            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(token);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/").toUriString();
            ResponseEntity<List<ProductsDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ProductsDTO>>() {});

            List<ProductsDTO> products = response.getBody();
            model.addAttribute("products", products);
            return "admin";
        } catch (Exception e) {
            logger.error("Error in viewAdminPage", e);
            return "error";
        }
    }

    

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute CreateProductCommand command,
                                BindingResult result,
                                Model model,
                                HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin";
        }

        try {
            String token = (String) request.getSession().getAttribute("token");
            if (token == null) {
                return "redirect:/auth/login";
            }

            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(token);

            if (command.getProductImageFile() != null && !command.getProductImageFile().isEmpty()) {
                String newImageUrl = s3Service.uploadFile(command.getProductImageFile());
                command.setProductImageUrl(newImageUrl);
            }

            HttpEntity<CreateProductCommand> requestEntity = new HttpEntity<>(command, headers);
            String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/admin/create").toUriString();
            restTemplate.exchange(url, HttpMethod.POST, requestEntity, Void.class);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in createProduct", e);
            return "error";
        }
    }

    @PutMapping("/update/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId,
                                @Valid @ModelAttribute UpdateProductCommand command,
                                BindingResult result,
                                Model model,
                                HttpServletRequest request) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin";
        }

        try {
            String token = (String) request.getSession().getAttribute("token");
            if (token == null) {
                return "redirect:/auth/login";
            }

            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(token);
            headers.setContentType(MediaType.APPLICATION_JSON);

            if (command.getProductImageFile() != null && !command.getProductImageFile().isEmpty()) {
                String newImageUrl = s3Service.uploadFile(command.getProductImageFile());
                command.setProductImageUrl(newImageUrl);
            } else {
                command.setProductImageUrl(null);
            }

            command.setProductId(productId);
            HttpEntity<UpdateProductCommand> requestEntity = new HttpEntity<>(command, headers);
            String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/admin/update/" + productId).toUriString();
            restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in updateProduct", e);
            return "error";
        }
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute DeleteProductCommand command, BindingResult result, Model model,HttpServletRequest request) {
        try {
	        	if (result.hasErrors()) {
	                model.addAttribute("errors", result.getAllErrors());
	                return "admin";
	            }
	        	
	        	String token = (String) request.getSession().getAttribute("token");
	            if (token == null) {
	                return "redirect:/auth/login";
	            }
	            
 	            HttpHeaders headers = new HttpHeaders();
 	            
	            command.setProductId(productId);
	            HttpEntity<DeleteProductCommand> requestEntity = new HttpEntity<>(command, headers);
	            String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/delete/" + productId).toUriString();
	            restTemplate.exchange(url, HttpMethod.DELETE, requestEntity, Void.class);
	            return "redirect:/admin/";
            
        } catch (Exception e) {
            logger.error("Error in deleteProduct", e);
            return "error";
        }
    }
}
