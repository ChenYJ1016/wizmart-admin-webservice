package com.capstone.wizmart_admin_webservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.capstone.wizmart_admin_webservice.dto.CreateProductCommand;
import com.capstone.wizmart_admin_webservice.dto.DeleteProductCommand;
import com.capstone.wizmart_admin_webservice.dto.UpdateProductCommand;
import com.capstone.wizmart_admin_webservice.services.ProductCommandService;
import com.capstone.wizmart_admin_webservice.services.ProductQueryService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class ProductController {
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private ProductCommandService productCommandService;
    
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute CreateProductCommand command, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("products", productQueryService.getAllProducts());
            return "admin";
        }
        try {
            productCommandService.createProduct(command);
            return "redirect:/admin/";
        } catch (Exception e) {
            logger.error("Error in createProduct", e);
            return "error";
        }
    }

    @PutMapping("/update/{productId}")
    public String updateProduct(@PathVariable("productId") Long productId, @Valid @ModelAttribute UpdateProductCommand command, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result.getAllErrors());
            model.addAttribute("products", productQueryService.getAllProducts());
            return "admin";
        }
        try {
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
