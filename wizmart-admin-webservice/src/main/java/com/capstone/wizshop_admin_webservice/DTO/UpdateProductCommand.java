package com.capstone.wizshop_admin_webservice.DTO;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateProductCommand {
    private Long productId;

    @NotNull(message = "Product name cannot be null")
    private String productName;

    @NotNull(message = "Product description cannot be null")
    private String productDescription;

    @Min(value = 0, message = "Product price must be non-negative")
    private double productPrice;

    private String productImageUrl;

    @JsonIgnore
    private MultipartFile productImageFile;

    @NotNull(message = "Product colour must be valid")
    private String productColour;

    @NotNull(message = "Target gender for the product must be valid")
    private String productGender;

    private String productCategory;

    private List<SizeQuantities> sizeQuantities;

    // Getters and Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = HtmlUtils.htmlEscape(productName.trim());
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = HtmlUtils.htmlEscape(productDescription.trim());
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double price) {
        this.productPrice = price;
    }

    public String getProductColour() {
        return productColour;
    }

    public void setProductColour(String productColour) {
        this.productColour = HtmlUtils.htmlEscape(productColour.trim());
    }

    public String getProductGender() {
        return productGender;
    }

    public void setProductGender(String productGender) {
        this.productGender = HtmlUtils.htmlEscape(productGender.trim());
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String category) {
        this.productCategory = HtmlUtils.htmlEscape(category.trim());
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

    public List<SizeQuantities> getSizeQuantities() {
        return sizeQuantities;
    }

    public void setSizeQuantities(List<SizeQuantities> sizeQuantities) {
        this.sizeQuantities = sizeQuantities;
    }
}
