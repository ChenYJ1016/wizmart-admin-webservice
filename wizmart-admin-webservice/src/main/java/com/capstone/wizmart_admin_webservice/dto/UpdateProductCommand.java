package com.capstone.wizmart_admin_webservice.dto;

import org.springframework.web.multipart.MultipartFile;

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
    
    private String productImageUrl;

    private MultipartFile productImageFile;
    
    @NotNull(message = "Product colour must be valid")
    private String productColour;
    
    @NotNull(message = "Target gender for the product must be valid")
    private String gender;
    
    private String size;
    
    private String category;

    // Getters and Setters

    public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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


	public String getProductColour() {
		return productColour;
	}

	public void setProductColour(String productColour) {
		this.productColour = productColour;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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