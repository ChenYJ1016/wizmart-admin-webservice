package com.capstone.wizshop_admin_webservice.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import com.capstone.wizshop_admin_webservice.dto.CreateProductCommand;
import com.capstone.wizshop_admin_webservice.dto.DeleteProductCommand;
import com.capstone.wizshop_admin_webservice.dto.UpdateProductCommand;
import com.capstone.wizshop_admin_webservice.listener.ProductEventListener;
import com.capstone.wizshop_admin_webservice.model.Event;
import com.capstone.wizshop_admin_webservice.repositories.EventRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.capstone.wizshop_admin_webservice.dto.ProductEventPayload;

@Component
public class ProductCommandHandler {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventListener.class);
    
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper; // Inject ObjectMapper

    public void handleCreateProductCommand(CreateProductCommand command) {
        try {
            ProductEventPayload payload = new ProductEventPayload();
            payload.setProductName(command.getProductName());
            payload.setProductDescription(command.getProductDescription());
            payload.setProductPrice(command.getProductPrice());
            payload.setProductQuantity(command.getProductQuantity());
            payload.setProductImageFileName(command.getProductImageUrl());
            payload.setProductColour(command.getProductColour());
            payload.setProductGender(command.getProductGender());
            payload.setProductSize(command.getProductSize());
            payload.setProductCategory(command.getProductCategory());

            String payloadJson = objectMapper.writeValueAsString(payload);

            Event event = new Event("PRODUCT_CREATED", payloadJson);
            eventRepository.save(event);
            eventPublisher.publishEvent(event);

            logger.info("Create event published");
        } catch (Exception e) {
            logger.error("Error creating PRODUCT_CREATED event: ", e);
        }
    }

    public void handleUpdateProductCommand(UpdateProductCommand command) {
        try {
            ProductEventPayload payload = new ProductEventPayload();
            payload.setProductId(command.getProductId().toString());
            payload.setProductName(command.getProductName());
            payload.setProductDescription(command.getProductDescription());
            payload.setProductPrice(command.getProductPrice());
            payload.setProductQuantity(command.getProductQuantity());
            payload.setProductImageFileName(command.getProductImageUrl());
            payload.setProductColour(command.getProductColour());
            payload.setProductGender(command.getProductGender());
            payload.setProductSize(command.getProductSize());
            payload.setProductCategory(command.getProductCategory());

            String payloadJson = objectMapper.writeValueAsString(payload);

            Event event = new Event("PRODUCT_UPDATED", payloadJson);
            eventRepository.save(event);
            eventPublisher.publishEvent(event);

            logger.info("Update event published");
        } catch (Exception e) {
            logger.error("Error creating PRODUCT_UPDATED event: ", e);
        }
    }

    public void handleDeleteProductCommand(DeleteProductCommand command) {
        try {
            ProductEventPayload payload = new ProductEventPayload();
            payload.setProductId(command.getProductId().toString());

            String payloadJson = objectMapper.writeValueAsString(payload);

            Event event = new Event("PRODUCT_DELETED", payloadJson);
            eventRepository.save(event);
            eventPublisher.publishEvent(event);

            logger.info("Delete event published");
        } catch (Exception e) {
            logger.error("Error creating PRODUCT_DELETED event: ", e);
        }
    }
}
