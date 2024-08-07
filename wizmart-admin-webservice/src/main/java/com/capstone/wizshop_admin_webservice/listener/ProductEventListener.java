package com.capstone.wizshop_admin_webservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.capstone.wizshop_admin_webservice.model.Event;
import com.capstone.wizshop_admin_webservice.model.Products;
import com.capstone.wizshop_admin_webservice.repositories.EventRepository;
import com.capstone.wizshop_admin_webservice.repositories.ProductRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.capstone.wizshop_admin_webservice.dto.ProductEventPayload;

@Component
public class ProductEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventListener.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ObjectMapper objectMapper; // Inject ObjectMapper

    @EventListener
    public void onApplicationEvent(Event event) {
        try {
            logger.info("EventListener starting");
            String eventType = event.getType();
            String payloadJson = event.getPayload();

            logger.info("Received event of type: {}", eventType);
            logger.info("Event payload: {}", payloadJson);

            ProductEventPayload payload = objectMapper.readValue(payloadJson, ProductEventPayload.class);

            // Handle different types of events
            switch (eventType) {
                case "PRODUCT_UPDATED":
                    handleProductUpdatedEvent(payload);
                    break;
                case "PRODUCT_CREATED":
                    handleProductCreatedEvent(payload);
                    break;
                case "PRODUCT_DELETED":
                    handleProductDeletedEvent(payload);
                    break;
                default:
                    logger.warn("Unhandled event type: {}", eventType);
                    break;
            }

            // Optionally, save the event to the database for auditing or other purposes
            eventRepository.save(event);
        } catch (Exception e) {
            logger.error("Error handling event: ", e);
        }
    }

    private void handleProductUpdatedEvent(ProductEventPayload payload) {
        try {
            // Update the product in the read model
            Products product = productRepository.findById(Long.parseLong(payload.getProductId()))
                .orElseThrow(() -> new RuntimeException("Product not found: " + payload.getProductId()));
            product.setProductName(payload.getProductName());
            product.setProductDescription(payload.getProductDescription());
            product.setProductPrice(payload.getProductPrice());
            product.setProductQuantity(payload.getProductQuantity());
            product.setProductImageUrl(payload.getProductImageFileName()); // Update to use file name
            product.setProductColour(payload.getProductColour());
            product.setProductGender(payload.getProductGender());
            product.setProductSize(payload.getProductSize());
            product.setProductCategory(payload.getProductCategory());
            productRepository.save(product);

            logger.info("Product updated: {} {} {} {} {} {} {} {} {}", product.getProductName(), product.getProductDescription(), product.getProductPrice(), product.getProductQuantity(), product.getProductImageUrl(), product.getProductColour(), product.getProductGender(), product.getProductSize(), product.getProductCategory());
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_UPDATED event: ", e);
        }
    }

    private void handleProductCreatedEvent(ProductEventPayload payload) {
        try {
            // Create a new product
            Products newProduct = new Products();
            newProduct.setProductName(payload.getProductName());
            newProduct.setProductDescription(payload.getProductDescription());
            newProduct.setProductPrice(payload.getProductPrice());
            newProduct.setProductQuantity(payload.getProductQuantity());
            newProduct.setProductImageUrl(payload.getProductImageFileName()); // Update to use file name
            newProduct.setProductColour(payload.getProductColour());
            newProduct.setProductGender(payload.getProductGender());
            newProduct.setProductSize(payload.getProductSize());
            newProduct.setProductCategory(payload.getProductCategory());

            // Save the new product to the repository
            productRepository.save(newProduct);

            logger.info("Product created: {}", newProduct);
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_CREATED event: ", e);
        }
    }

    private void handleProductDeletedEvent(ProductEventPayload payload) {
        try {
            // Delete the product from the repository
            Long productId = Long.parseLong(payload.getProductId());
            productRepository.deleteById(productId);

            logger.info("Product deleted: {}", productId);
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_DELETED event: ", e);
        }
    }
}
