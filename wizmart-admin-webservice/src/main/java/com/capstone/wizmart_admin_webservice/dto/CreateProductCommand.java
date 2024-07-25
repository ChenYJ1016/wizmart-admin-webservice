package com.capstone.wizmart_admin_webservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateProductCommand {
    
	@NotNull(message = "Product name cannot be null")
    private String name;
    
	@NotNull(message = "Product description cannot be null")
    private String description;
    
    @Min(value = 0, message = "Product price must be non-negative")
    private double price;
    
    @Min(value = 0, message = "Product stock must be non-negative")
    private int quantity;
    
    private String productImageUrl;
    
    private String productColour;
    
    private String gender;
    
    private String size;
    
    private String category;

    // Getters and Setters

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
