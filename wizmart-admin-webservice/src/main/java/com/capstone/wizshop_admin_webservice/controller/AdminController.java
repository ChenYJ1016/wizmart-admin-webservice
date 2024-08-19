package com.capstone.wizshop_admin_webservice.controller;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.capstone.wizshop_admin_webservice.DTO.CreateProductCommand;
import com.capstone.wizshop_admin_webservice.DTO.Products;
import com.capstone.wizshop_admin_webservice.DTO.UpdateProductCommand;
import com.capstone.wizshop_admin_webservice.Services.Aws.AdminS3Service;
import com.capstone.wizshop_admin_webservice.properties.Properties;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private Properties properties;
    
    @Autowired
    private AdminS3Service s3Service;
    
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping
    public String redirectToAdmin() {
//        try {
            return "redirect:/admin/";
//        } catch (Exception e) {
//            logger.error("Error in redirectToAdmin", e);
//            return "error"; 
//        }
    }

    @GetMapping("/")
    public String viewAdminPage(Model model) {
//        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication instanceof OAuth2AuthenticationToken) {
                OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                String token = oidcUser.getIdToken().getTokenValue();

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token);

                HttpEntity<String> entity = new HttpEntity<>(headers);

                String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/").toUriString();
                ResponseEntity<List<Products>> response = restTemplate.exchange(url, HttpMethod.GET, entity, new ParameterizedTypeReference<List<Products>>() {});

                List<Products> products = response.getBody();
                model.addAttribute("products", products);
                return "admin";
            } else {
                return "redirect:/admin/";
            }
//        } catch (Exception e) {
//            logger.error("Error in viewAdminPage", e);
//            return "error";
//        }
    }

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute CreateProductCommand command, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin";
        }
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication instanceof OAuth2AuthenticationToken) {
                OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
                String token = oidcUser.getIdToken().getTokenValue();

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token);

                HttpEntity<CreateProductCommand> request = new HttpEntity<>(command, headers);
                String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/create").toUriString();
                restTemplate.postForEntity(url, request, Void.class);
                return "redirect:/admin/";
            } else {
                return "redirect:/admin/";
            }
        } catch (Exception e) {
            logger.error("Error in createProduct", e);
            return "error";
        }
    }
    @PutMapping("/update/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute UpdateProductCommand command, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            return "admin";
        }
//        try {	
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null && authentication instanceof OAuth2AuthenticationToken) {
	            OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
	            String token = oidcUser.getIdToken().getTokenValue();
	
	            HttpHeaders headers = new HttpHeaders();
	            headers.setBearerAuth(token);
	            headers.setContentType(MediaType.APPLICATION_JSON);

	
		        // Handle image file upload
		        if (command.getProductImageFile() != null && !command.getProductImageFile().isEmpty()) {
		            String newImageUrl = s3Service.uploadFile(command.getProductImageFile());
		            command.setProductImageUrl(newImageUrl);
		        } else {
		            command.setProductImageUrl(null);
		        }
		        
	            command.setProductId(productId);
	            HttpEntity<UpdateProductCommand> request = new HttpEntity<>(command, headers);
	            String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/update/" + productId).toUriString();
	            restTemplate.exchange(url, HttpMethod.PUT, request, Void.class);
	            return "redirect:/admin/";
	        }else {
                return "redirect:/admin/";
            }
//        } catch (Exception e) {
//            logger.error("Error in updateProduct", e);
//            return "error";
//        }
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        try {
            String url = UriComponentsBuilder.fromHttpUrl(properties.getCommonRepoUrl() + "/api/products/delete/" + productId).toUriString();
            restTemplate.exchange(url, HttpMethod.DELETE, null, Void.class);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in deleteProduct", e);
            return "error";
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
            return "error"; 
        }
    }

    private String generateState() {
        try {
            byte[] randomBytes = new byte[24];
            secureRandom.nextBytes(randomBytes);
            return base64Encoder.encodeToString(randomBytes);
        } catch (Exception e) {
            logger.error("Error in generateState", e);
            return null; 
        }
    }
}
