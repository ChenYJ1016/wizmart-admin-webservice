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
            String newName = parts[5];
            String newDescription = parts[6];
            String newPrice = parts[7];
            String newProductStock = parts[8];

            // Update the product in the read model (products table)
            Products product = productRepository.findById(Long.parseLong(productId)).orElseThrow(() -> new RuntimeException("Product not found: " + productId));
            product.setProductName(newName);
            product.setProductDescription(newDescription);
            product.setProductPrice(Double.parseDouble(newPrice));
            product.setProductStock(Integer.parseInt(newProductStock));
            productRepository.save(product);

            logger.info("Product updated: {}", product);
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_UPDATED event: ", e);
        }
    }

    private void handleProductCreatedEvent(String payload) {
        try {
            // Parse the event payload to extract product details
            // Assuming the payload is "Product <productId> created with name <productName> <productDescription> <productPrice> <productQuantity>"
            String[] parts = payload.split(" ");
            String productName = parts[4];
            String productDescription = parts[5];
            Double productPrice = Double.parseDouble(parts[6]);
            Integer productQuantity = Integer.parseInt(parts[7]);

            // Create a new product
            Products newProduct = new Products();
            newProduct.setProductName(productName);
            newProduct.setProductDescription(productDescription);
            newProduct.setProductPrice(productPrice);
            newProduct.setProductStock(productQuantity);

            // Save the new product to the repository
            productRepository.save(newProduct);

            logger.info("Product created: {}", newProduct);
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_CREATED event: ", e);
        }
    }

    private void handleProductDeletedEvent(String payload) {
        try {
            // Parse the event payload to extract product details
            String[] parts = payload.split(" ");
            String productId = parts[1];

            // Delete the product from the read model (products table)
            productRepository.deleteById(Long.parseLong(productId));

            logger.info("Product deleted: {}", productId);
        } catch (Exception e) {
            logger.error("Error handling PRODUCT_DELETED event: ", e);
        }
    }
}
