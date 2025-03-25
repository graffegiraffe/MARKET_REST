package com.tms.repository;

import com.tms.config.DatabaseService;
import com.tms.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tms.config.SQLQuery.CREATE_PRODUCT;
import static com.tms.config.SQLQuery.DELETE_PRODUCT;
import static com.tms.config.SQLQuery.GET_ALL_PRODUCTS;
import static com.tms.config.SQLQuery.GET_PRODUCT_BY_ID;
import static com.tms.config.SQLQuery.UPDATE_PRODUCT;

@Repository
public class ProductRepository {

    private final DatabaseService databaseService;

    @Autowired
    public ProductRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Optional<Product> getProductById(Integer id) {
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(GET_PRODUCT_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(parseProduct(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<Integer> createProduct(Product product) {
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement( CREATE_PRODUCT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, product.getName());
            statement.setDouble(2, product.getPrice());
            statement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            statement.setTimestamp(4, null);
            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return Optional.of(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public boolean updateProduct(Product product) {
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
        try (Connection connection = databaseService.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
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