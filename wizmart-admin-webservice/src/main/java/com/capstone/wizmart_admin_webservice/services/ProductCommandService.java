package com.capstone.wizmart_admin_webservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.capstone.wizmart_admin_webservice.handlers.ProductCommandHandler;
import com.capstone.wizmart_admin_webservice.model.commands.CreateProductCommand;
import com.capstone.wizmart_admin_webservice.model.commands.DeleteProductCommand;
import com.capstone.wizmart_admin_webservice.model.commands.UpdateProductCommand;

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