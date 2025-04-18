package com.tms.controller;

import com.tms.model.Product;
import com.tms.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product created successfully."),
            @ApiResponse(responseCode = "409", description = "Conflict during product creation.")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Received request to create product: {}", product);

        Optional<Product> createdProduct = productService.createProduct(product);
        if (createdProduct.isPresent()) {
            logger.info("Product created successfully: {}", createdProduct.get());
            return new ResponseEntity<>(createdProduct.get(), HttpStatus.CREATED);
        } else {
            logger.error("Failed to create product: {}", product);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product fetched successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable("id") @Parameter(description = "Product ID") Long id) {
        logger.info("Received request to fetch product with ID: {}", id);

        return productService.getProductById(id)
                .map(product -> {
                    logger.info("Product fetched successfully: {}", product);
                    return new ResponseEntity<>(product, HttpStatus.OK);
                })
                .orElseGet(() -> {
                    logger.warn("Product with ID {} not found.", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All products fetched successfully.")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Received request to fetch all products.");

        List<Product> products = productService.getAllProducts();
        if (!products.isEmpty()) {
            logger.info("Successfully fetched all products.");
            return new ResponseEntity<>(products, HttpStatus.OK);
        } else {
            logger.warn("No products found.");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product updated successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found."),
            @ApiResponse(responseCode = "409", description = "Conflict during product update.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") Long id, @RequestBody Product product) {
        logger.info("Received request to update product with ID: {}", id);

        product.setId(id); // Обновляем ID продукта из пути
        Optional<Product> updatedProduct = productService.updateProduct(product);

        if (updatedProduct.isPresent()) {
            logger.info("Product successfully updated: {}", updatedProduct.get());
            return new ResponseEntity<>(updatedProduct.get(), HttpStatus.OK);
        } else {
            logger.warn("Failed to update product with ID: {}. Product not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Product deleted successfully."),
            @ApiResponse(responseCode = "404", description = "Product not found.")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(
            @PathVariable("id") @Parameter(description = "Product ID") Long id) {
        logger.info("Received request to delete product with ID: {}", id);

        boolean deleted = productService.deleteProduct(id);

        if (deleted) {
            logger.info("Product with ID {} deleted successfully.", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            logger.warn("Product with ID {} not found.", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}