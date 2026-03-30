package com.iliana.demo.service;

import com.iliana.demo.model.Product;
import com.iliana.demo.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    public List<Product> getAllProducts() {
        logger.info("Service: Fetching all products from database");
        List<Product> products = productRepository.findAll();
        logger.info("Service: Found {} products", products.size());
        return products;
    }

    // Get product by ID
    public Optional<Product> getProductById(Long id) {
        logger.info("Service: Fetching product with ID: {}", id);
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            logger.info("Service: Product found - Name: {}, Price: {}", product.get().getName(), product.get().getPrice());
        } else {
            logger.warn("Service: Product with ID {} not found", id);
        }
        return product;
    }

    // Create a new product
    public Product createProduct(Product product) {
        logger.info("Service: Creating new product - Name: {}, Price: {}, Description: {}",
                product.getName(), product.getPrice(), product.getDescription());

        // Basic validation - price cannot be negative
        if (product.getPrice() != null && product.getPrice() < 0) {
            logger.warn("Service: Validation failed - Price cannot be negative! Received: {}", product.getPrice());
            throw new IllegalArgumentException("Price cannot be negative");
        }

        logger.info("Service: Validation passed - proceeding to save product");
        Product savedProduct = productRepository.save(product);
        logger.info("Service: Product created successfully with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    // Update an existing product
    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        logger.info("Service: Updating product with ID: {}", id);

        return productRepository.findById(id)
                .map(product -> {
                    logger.info("Service: Product found - Current Name: {}, Current Price: {}",
                            product.getName(), product.getPrice());
                    logger.info("Service: Updating to - New Name: {}, New Price: {}, New Description: {}",
                            updatedProduct.getName(), updatedProduct.getPrice(), updatedProduct.getDescription());

                    product.setName(updatedProduct.getName());
                    product.setPrice(updatedProduct.getPrice());
                    product.setDescription(updatedProduct.getDescription());

                    Product saved = productRepository.save(product);
                    logger.info("Service: Product with ID {} updated successfully", id);
                    return saved;
                })
                .or(() -> {
                    logger.warn("Service: Product with ID {} not found - update failed", id);
                    return Optional.empty();
                });
    }

    // Delete a product
    public void deleteProduct(Long id) {
        logger.info("Service: Deleting product with ID: {}", id);
        productRepository.deleteById(id);
        logger.info("Service: Product with ID {} deleted", id);
    }
}