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
            @ApiResponse(description = "Product created successfully", responseCode = "201"),
            @ApiResponse(description = "Conflict during product creation", responseCode = "409")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        logger.info("Received request to create a product: {}", product);
        Optional<Product> createdProduct = productService.createProduct(product);
        if (createdProduct.isEmpty()) {
            logger.error("Failed to create product: {}", product);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        logger.info("Product created successfully: {}", createdProduct.get());
        return new ResponseEntity<>(createdProduct.get(), HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Product fetched successfully", responseCode = "200"),
            @ApiResponse(description = "Product not found", responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") @Parameter(description = "Product ID") Integer id) {
        logger.info("Received request to fetch product by ID: {}", id);
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            logger.warn("Product with ID {} not found", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Product with ID {} fetched successfully: {}", id, product.get());
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "All products fetched successfully", responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        logger.info("Received request to fetch all products");
        List<Product> products = productService.getAllProducts();
        logger.info("All products fetched successfully. Total products: {}", products.size());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Product updated successfully", responseCode = "200"),
            @ApiResponse(description = "Conflict during product update", responseCode = "409")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id, @RequestBody Product product) {
        logger.info("Received request to update product with ID {}: {}", id, product);
        product.setId(id);
        Optional<Product> updatedProduct = productService.updateProduct(product);
        if (updatedProduct.isEmpty()) {
            logger.error("Failed to update product with ID {}: {}", id, product);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        logger.info("Product with ID {} updated successfully: {}", id, updatedProduct.get());
        return new ResponseEntity<>(updatedProduct.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") @Parameter(description = "Product ID") Integer id) {
        logger.info("Received request to delete product with ID: {}", id);
        boolean isDeleted = productService.deleteProduct(id);
        if (!isDeleted) {
            logger.warn("Product with ID {} not found for deletion", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        logger.info("Product with ID {} deleted successfully", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}