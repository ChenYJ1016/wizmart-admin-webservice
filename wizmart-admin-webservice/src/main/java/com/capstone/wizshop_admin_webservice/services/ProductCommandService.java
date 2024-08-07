package com.capstone.wizshop_admin_webservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.wizshop_admin_webservice.dto.CreateProductCommand;
import com.capstone.wizshop_admin_webservice.dto.DeleteProductCommand;
import com.capstone.wizshop_admin_webservice.dto.UpdateProductCommand;
import com.capstone.wizshop_admin_webservice.handlers.ProductCommandHandler;

@Service
public class ProductCommandService {

    @Autowired
    private ProductCommandHandler productCommandHandler;

    public void createProduct(CreateProductCommand command) {
        productCommandHandler.handleCreateProductCommand(command);
    }

    public void updateProduct(UpdateProductCommand command) {
        productCommandHandler.handleUpdateProductCommand(command);
    }

    public void deleteProduct(DeleteProductCommand command) {
        productCommandHandler.handleDeleteProductCommand(command);
    }
}