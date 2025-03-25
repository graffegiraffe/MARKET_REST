package com.tms.controller;

import com.tms.model.Product;
import com.tms.service.ProductService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

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
        Optional<Product> createdProduct = productService.createProduct(product);
        if (createdProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(createdProduct.get(), HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Product fetched successfully", responseCode = "200"),
            @ApiResponse(description = "Product not found", responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") @Parameter(description = "Product ID") Integer id) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product.get(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "All products fetched successfully", responseCode = "200")
    })
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(description = "Product updated successfully", responseCode = "200"),
            @ApiResponse(description = "Conflict during product update", responseCode = "409")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Integer id, @RequestBody Product product) {
        product.setId(id);
        Optional<Product> updatedProduct = productService.updateProduct(product);
        if (updatedProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(updatedProduct.get(), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProduct(@PathVariable("id") @Parameter(description = "Product ID") Integer id) {
        boolean isDeleted = productService.deleteProduct(id);
        if (!isDeleted) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}