package com.capstone.wizmart_admin_webservice.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.capstone.wizmart_admin_webservice.dto.CreateProductCommand;
import com.capstone.wizmart_admin_webservice.dto.DeleteProductCommand;
import com.capstone.wizmart_admin_webservice.dto.UpdateProductCommand;
import com.capstone.wizmart_admin_webservice.listener.ProductEventListener;
import com.capstone.wizmart_admin_webservice.model.Event;
import com.capstone.wizmart_admin_webservice.repositories.EventRepository;

@Component
public class ProductCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(ProductEventListener.class);
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;

    @Autowired
    private EventRepository eventRepository;

    public void handleCreateProductCommand(CreateProductCommand command) {

        // Emit event
        Event event = new Event();
        event.setType("PRODUCT_CREATED");
        event.setPayload(String.format("Product created with name %s %s %f %d %s %s %s %s %s", command.getProductName(), command.getProductDescription(), command.getProductPrice(), command.getProductQuantity(), command.getProductImageUrl(), command.getProductColour(), command.getProductGender(), command.getProductSize(), command.getProductCategory()));
        eventRepository.save(event);
        
        eventPublisher.publishEvent(event);
        logger.info("Create event published");
        
        
    }

    public void handleUpdateProductCommand(UpdateProductCommand command) {
        // Perform validation, business logic, etc. for updating a product

        // Emit event
        Event event = new Event();
        event.setType("PRODUCT_UPDATED");
        event.setPayload(String.format("Product %s updated with name %s %s %f %d %s %s %s %s %s", command.getProductId(), command.getName(), command.getDescription(), command.getPrice(), command.getQuantity(), command.getProductImageUrl(), command.getProductColour(), command.getGender(), command.getSize(), command.getCategory()));
        eventRepository.save(event);
        
        eventPublisher.publishEvent(event);
       
    }

    public void handleDeleteProductCommand(DeleteProductCommand command) {
        // Perform validation, business logic, etc. for deleting a product

        // Emit event
        Event event = new Event();
        event.setType("PRODUCT_DELETED");
        event.setPayload("Product " + command.getProductId() + " deleted");
        eventRepository.save(event);
        
        eventPublisher.publishEvent(event);

    }
}
