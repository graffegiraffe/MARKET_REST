package com.tms.config;

public interface SQLQuery {
    String GET_USER_BY_ID = "SELECT * FROM users WHERE id = ?";
    String FIND_ALL_USERS = "SELECT * FROM users WHERE is_deleted = false";
    String DELETE_PRODUCT = "DELETE FROM product WHERE id = ?";
    String GET_ALL_PRODUCTS = "SELECT * FROM product";
    String GET_SECURITY_BY_LOGIN = "SELECT * FROM security WHERE login = ?";
    String DELETE_SECURITY = "DELETE FROM security WHERE id = ?";
    String GET_SECURITY_BY_ID = "SELECT * FROM security WHERE id = ?";

    String CREATE_SECURITY_WEB = "INSERT INTO security (login, password, role, created, user_id) VALUES (?, ?, ?, ?, ?)";
    String UPDATE_SECURITY = "UPDATE security SET login = ?, password = ?, role = ?, updated = ?, user_id = ? WHERE id = ?";

    String GET_PRODUCT_BY_ID = "SELECT * FROM product WHERE id = ?";
    String CREATE_PRODUCT = "INSERT INTO product (name, price, created, updated) VALUES (?, ?, ?, ?)";
    String UPDATE_PRODUCT = "UPDATE product SET name = ?, price = ?, updated = ? WHERE id = ?";

    String DELETE_USER =  "UPDATE users SET is_deleted = TRUE WHERE id = ?";
    String UPDATE_USER = "UPDATE users SET firstname=?,second_name=?,age=?,telephone_number=?,email=?,sex=?,updated=DEFAULT WHERE id=?";
    String CREATE_USER = "INSERT INTO users (id, firstname, second_name, age, telephone_number, email, created, updated, sex, is_deleted) " +
            "VALUES (DEFAULT, ?, ?, ?, ?, ?, DEFAULT, ?, ?, ?)";
    String CREATE_SECURITY = "INSERT INTO security (id, login, password, role, created, updated, user_id) " +
            "VALUES (DEFAULT, ?, ?, ?, DEFAULT, ?, ?)";
}
