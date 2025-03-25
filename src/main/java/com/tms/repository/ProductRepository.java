package com.tms.repository;

import com.tms.config.DatabaseService;
import com.tms.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.tms.config.SQLQuery.CREATE_PRODUCT;
import static com.tms.config.SQLQuery.DELETE_PRODUCT;
import static com.tms.config.SQLQuery.GET_ALL_PRODUCTS;
import static com.tms.config.SQLQuery.GET_PRODUCT_BY_ID;
import static com.tms.config.SQLQuery.UPDATE_PRODUCT;

@Repository
public class ProductRepository {
    private static final Logger logger = LoggerFactory.getLogger(ProductRepository.class);

    private final DatabaseService databaseService;

    @Autowired
    public ProductRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
        logger.info("ProductRepository initialized with DatabaseService");
    }

    public Optional<Product> getProductById(Integer id) {
        logger.info("Fetching product with ID: {}", id);
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PRODUCT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Product product = parseProduct(resultSet);
                logger.info("Product found with ID {}: {}", id, product);
                return Optional.of(parseProduct(resultSet));
            }else {
                logger.warn("No product found with ID: {}", id);
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error("Error fetching product with ID: {}. Error: {}", id, e.getMessage());
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Integer> createProduct(Product product) {
        logger.info("Creating product: {}", product);
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement( CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(4, null);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    logger.info("Product created successfully with ID: {}", generatedId);
                    return Optional.of(generatedId);
                }
            }
            logger.error("Failed to create product: {}", product);
            return Optional.empty();

        } catch (SQLException e) {
            logger.error("Error creating product: {}. Error: {}", product, e.getMessage());
            return Optional.empty();
        }
    }

    public boolean updateProduct(Product product) {
        logger.info("Updating product: {}", product);
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setInt(4, product.getId());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(Integer id) {
        logger.info("Deleting product with ID: {}", id);
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT)) {
            statement.setInt(1, id);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Product deleted successfully with ID: {}", id);
                return true;
            } else {
                logger.warn("No product deleted for ID: {}", id);
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error deleting product with ID: {}. Error: {}", id, e.getMessage());
            return false;
        }
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_ALL_PRODUCTS);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                products.add(parseProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    private Product parseProduct(ResultSet resultSet) throws SQLException {
        return new Product(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getDouble("price"),
                resultSet.getTimestamp("created"),
                resultSet.getTimestamp("updated")
        );
    }
}