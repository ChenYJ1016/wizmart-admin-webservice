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
            // Parse the event payload to extract product details
        	String[] parts = payload.split(" ");
        	String productId = parts[1];
            String productName = parts[5];
            String productDescription = parts[6];
            Double productPrice = Double.parseDouble(parts[7]);
            int productQuantity = Integer.parseInt(parts[8]);
            String productImageUrl = parts[9];
            String productColour = parts[10];
            String productGender = parts[11];
            String productSize = parts[12];
            String productCategory = parts[13];

            // Update the product in the read model 
            Products product = productRepository.findById(Long.parseLong(productId)).orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            product.setProductName(productName);
            product.setProductDescription(productDescription);
            product.setProductPrice(productPrice);
            product.setProductQuantity(productQuantity);
            product.setProductImageUrl(productImageUrl);
            product.setProductColour(productColour);
            product.setProductGender(productGender);
            product.setProductSize(productSize);
            product.setProductCategory(productCategory);
            productRepository.save(product);

            logger.info("Product updated: {} {} {} {} {} {} {} {} {}", product.getProductName(), product.getProductDescription(), product.getProductPrice(), product.getProductQuantity(), product.getProductImageUrl(), product.getProductColour(), product.getProductGender(), product.getProductSize(), product.getProductCategory());   
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_UPDATED event: ", e);
        }
    }

    private void handleProductCreatedEvent(String payload) {
        try {
            // Parse the event payload to extract product details
            String[] parts = payload.split(" ");
            String productName = parts[4];
            String productDescription = parts[5];
            Double productPrice = Double.parseDouble(parts[6]);
            Integer productQuantity = Integer.parseInt(parts[7]);
            String productImageUrl = parts[8];
            String productColour = parts[9];
            String productGender = parts[10];
            String productSize = parts[11];
            String productCategory = parts[12];

            // Create a new product
            Products newProduct = new Products();
            newProduct.setProductName(productName);
            newProduct.setProductDescription(productDescription);
            newProduct.setProductPrice(productPrice);
            newProduct.setProductQuantity(productQuantity);
            newProduct.setProductImageUrl(productImageUrl);
            newProduct.setProductColour(productColour);
            newProduct.setProductGender(productGender);
            newProduct.setProductSize(productSize);
            newProduct.setProductCategory(productCategory);

            // Save the new product to the repository
            productRepository.save(newProduct);

            logger.info("Product created: {}", newProduct);
           }catch(Exception e) {
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
