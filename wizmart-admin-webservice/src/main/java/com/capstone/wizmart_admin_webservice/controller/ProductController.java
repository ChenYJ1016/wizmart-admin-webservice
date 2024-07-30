package com.capstone.wizmart_admin_webservice.controller;

import com.capstone.wizmart_admin_webservice.dto.CreateProductCommand;
import com.capstone.wizmart_admin_webservice.dto.DeleteProductCommand;
import com.capstone.wizmart_admin_webservice.dto.UpdateProductCommand;
import com.capstone.wizmart_admin_webservice.services.ProductCommandService;
import com.capstone.wizmart_admin_webservice.services.ProductQueryService;
import com.capstone.wizmart_admin_webservice.services.aws.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private ProductCommandService productCommandService;

    @Autowired
    private S3Service s3Service;

    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute CreateProductCommand command, BindingResult result, Model model) throws IOException {
        logger.info("Creating product...");
        if (result.hasErrors()) {
            logger.error("Validation errors occurred: {}", result.getAllErrors());
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("products", productQueryService.getAllProducts());
            return "admin";
        }
        try {
            logger.info("Uploading image");
            // Handle image file upload
            if (command.getProductImageFile() != null && !command.getProductImageFile().isEmpty()) {
                String imageUrl = s3Service.uploadFile(command.getProductImageFile());
                command.setProductImageUrl(imageUrl);
            }

            productCommandService.createProduct(command);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in createProduct", e);
            return "error";
        }
    }

    @PutMapping("/update/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute UpdateProductCommand command, BindingResult result, Model model) throws IOException {
    	logger.info("updating....");

    	if (result.hasErrors()) {
            logger.error("Validation errors occurred: {}", result.getAllErrors());
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("products", productQueryService.getAllProducts());
            return "admin";
        }
        try {
            // Handle image file upload
            if (command.getProductImageFile() != null && !command.getProductImageFile().isEmpty()) {
                String imageUrl = s3Service.uploadFile(command.getProductImageFile());
                command.setProductImageUrl(imageUrl);
            }

            command.setProductId(productId);
            productCommandService.updateProduct(command);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in updateProduct", e);
            return "error";
        }
    }

    @DeleteMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable("productId") Long productId) {
        try {
            DeleteProductCommand command = new DeleteProductCommand();
            command.setProductId(productId);
            productCommandService.deleteProduct(command);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in deleteProduct", e);
            return "error";
        }
    }
}
