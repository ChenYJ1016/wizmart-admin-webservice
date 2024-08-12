package com.capstone.wizshop_admin_webservice.dto;

import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateProductCommand {
    
    @NotNull(message = "Product name cannot be null")
    private String productName;
    
    @NotNull(message = "Product description cannot be null")
    private String productDescription;
    
    @Min(value = 0, message = "Product price must be non-negative")
    private double productPrice;
    
    @Min(value = 0, message = "Product quantity must be non-negative")
    private int productQuantity;
    
    private String productImageUrl;
    
    private MultipartFile productImageFile;
    
    @NotNull(message = "Product colour must be valid")
    private String productColour;
    
    @NotNull(message = "Target gender for the product must be valid")
    private String productGender;
    
    private String productSize;
    
    private String productCategory;

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductColour() {
        return productColour;
    }

    public void setProductColour(String productColour) {
        this.productColour = productColour;
    }

    public String getProductGender() {
        return productGender;
    }

    public void setProductGender(String productGender) {
        this.productGender = productGender;
    }

    public String getProductSize() {
        return productSize;
    }

    public void setProductSize(String productSize) {
        this.productSize = productSize;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public MultipartFile getProductImageFile() {
        return productImageFile;
    }

    public void setProductImageFile(MultipartFile productImageFile) {
        this.productImageFile = productImageFile;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }
}