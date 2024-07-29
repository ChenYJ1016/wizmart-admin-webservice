package com.capstone.wizmart_admin_webservice.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.capstone.wizmart_admin_webservice.model.Event;
import com.capstone.wizmart_admin_webservice.model.Products;
import com.capstone.wizmart_admin_webservice.repositories.EventRepository;
import com.capstone.wizmart_admin_webservice.repositories.ProductRepository;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ProductEventListener {

    private static final Logger logger = LoggerFactory.getLogger(ProductEventListener.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EventRepository eventRepository;

    @EventListener
    public void onApplicationEvent(Event event) {
        try {
            logger.info("EventListener starting");
            String eventType = event.getType();
            String payload = event.getPayload();

            logger.info("Received event of type: {}", eventType);
            logger.info("Event payload: {}", payload);

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

    private void handleProductUpdatedEvent(String payload) {
        try {
            // Use regex or a more robust parsing method to extract details from the payload
            Pattern pattern = Pattern.compile("Product (\\d+) updated with name (.+), description (.+), price (\\d+\\.\\d+), quantity (\\d+)");
            Matcher matcher = pattern.matcher(payload);

            if (matcher.find()) {
                Long productId = Long.parseLong(matcher.group(1));
                String productName = matcher.group(2);
                String productDescription = matcher.group(3);
                Double productPrice = Double.parseDouble(matcher.group(4));
                Integer productQuantity = Integer.parseInt(matcher.group(5));

                // Update the product in the repository
                Products product = productRepository.findById(productId)
                        .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
                product.setProductName(productName);
                product.setProductDescription(productDescription);
                product.setProductPrice(productPrice);
                product.setProductStock(productQuantity);
                productRepository.save(product);

                logger.info("Product updated: {}", product);
            } else {
                logger.warn("Payload format is incorrect for PRODUCT_UPDATED event: {}", payload);
            }
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_UPDATED event: ", e);
        }
    }

    private void handleProductCreatedEvent(String payload) {
        try {
            // Use regex or a more robust parsing method to extract details from the payload
            Pattern pattern = Pattern.compile("Product created with name (.+), description (.+), price (\\d+\\.\\d+), quantity (\\d+), colour (.+), gender (.+), size (.+), category (.+), image URL (.+)");
            Matcher matcher = pattern.matcher(payload);

            if (matcher.find()) {
                String productName = matcher.group(1);
                String productDescription = matcher.group(2);
                Double productPrice = Double.parseDouble(matcher.group(3));
                Integer productQuantity = Integer.parseInt(matcher.group(4));
                String productColour = matcher.group(5);
                String gender = matcher.group(6);
                String size = matcher.group(7);
                String category = matcher.group(8);
                String imageUrl = matcher.group(9);

                // Create and save the new product
                Products newProduct = new Products();
                newProduct.setProductName(productName);
                newProduct.setProductDescription(productDescription);
                newProduct.setProductPrice(productPrice);
                newProduct.setProductStock(productQuantity);
                newProduct.setProductColour(productColour);
                newProduct.setGender(gender);
                newProduct.setSize(size);
                newProduct.setCategory(category);
                newProduct.setProductImageUrl(imageUrl);

                productRepository.save(newProduct);

                logger.info("Product created: {}", newProduct);
            } else {
                logger.warn("Payload format is incorrect for PRODUCT_CREATED event: {}", payload);
            }
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_CREATED event: ", e);
        }
    }

    private void handleProductDeletedEvent(String payload) {
        try {
            // Use regex or a more robust parsing method to extract details from the payload
            Pattern pattern = Pattern.compile("Product (\\d+) deleted");
            Matcher matcher = pattern.matcher(payload);

            if (matcher.find()) {
                Long productId = Long.parseLong(matcher.group(1));

                // Delete the product from the repository
                productRepository.deleteById(productId);

                logger.info("Product deleted: {}", productId);
            } else {
                logger.warn("Payload format is incorrect for PRODUCT_DELETED event: {}", payload);
            }
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_DELETED event: ", e);
        }
    }
}
