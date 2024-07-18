package com.capstone.wizmart_admin_webservice.model.commands;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class UpdateProductCommand {
    private Long productId;
    
    @NotNull(message = "Product name cannot be null")
    private String name;
    
	@NotNull(message = "Product description cannot be null")
    private String description;
    
    @Min(value = 0, message = "Product price must be non-negative")
    private double price;
    
    @Min(value = 0, message = "Product stock must be non-negative")
    private int quantity;

    // Getters and Setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long id) {
        this.productId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}