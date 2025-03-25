package com.tms.service;

import com.tms.model.Product;
import com.tms.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.getProductById(id);
    }

    public Optional<Product> createProduct(Product product) {
        Optional<Integer> productId = productRepository.createProduct(product);
        if (productId.isPresent()) {
            return getProductById(productId.get());
        }
        return Optional.empty();
    }

    public Optional<Product> updateProduct(Product product) {
        boolean updated = productRepository.updateProduct(product);
        if (updated) {
            return getProductById(product.getId());
        }
        return Optional.empty();
    }

    public boolean deleteProduct(Integer id) {
        return productRepository.deleteProduct(id);
    }
}