package com.tms.repository;

import com.tms.config.DatabaseService;
import com.tms.config.SQLQuery;
import com.tms.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Optional;

@Repository
public class UserRepository {

    private final DatabaseService databaseService;

    @Autowired
    public UserRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Optional<User> getUserById(Long id) {
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement getUserStatement = connection.prepareStatement(SQLQuery.GET_USER_BY_ID);
            getUserStatement.setLong(1, id);
            ResultSet resultSet = getUserStatement.executeQuery();
            return parseUser(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Boolean deleteUser(Long id){
        Connection connection = databaseService.getConnection();
        try {
            PreparedStatement getUserStatement = connection.prepareStatement(SQLQuery.DELETE_USER);
            getUserStatement.setLong(1, id);
            return getUserStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Optional<Long> createUser(User user){
        Connection connection = databaseService.getConnection();
        Long userId = null;

        try {
            PreparedStatement createUserStatement = connection.prepareStatement(SQLQuery.CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            createUserStatement.setString(1, user.getFirstname());
            createUserStatement.setString(2, user.getSecondName());
            createUserStatement.setInt(3, user.getAge());
            createUserStatement.setString(4, user.getTelephoneNumber());
            createUserStatement.setString(5, user.getEmail());
            createUserStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            createUserStatement.setString(7, user.getSex());
            createUserStatement.setBoolean(8, false);
            createUserStatement.executeUpdate();

            ResultSet generatedKeys = createUserStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                userId = generatedKeys.getLong(1);
            }
            return Optional.of(userId);
        } catch (SQLException e){
            System.out.println(e.getMessage());
            return Optional.empty();
        }
    }

    public Boolean updateUser(User user) {
        Connection connection = databaseService.getConnection();

        try {
            PreparedStatement getUserStatement = connection.prepareStatement(SQLQuery.UPDATE_USER);
            getUserStatement.setString(1, user.getFirstname());
            getUserStatement.setString(2, user.getSecondName());
            getUserStatement.setInt(3, user.getAge());
            getUserStatement.setString(4, user.getTelephoneNumber());
            getUserStatement.setString(5, user.getEmail());
            getUserStatement.setString(6, user.getSex());
            getUserStatement.setLong(7, user.getId());
            return getUserStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Optional<User> parseUser(ResultSet resultSet) throws SQLException {
        if (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setFirstname(resultSet.getString("firstname"));
            user.setSecondName(resultSet.getString("second_name"));
            user.setAge(resultSet.getInt("age"));
            user.setTelephoneNumber(resultSet.getString("telephone_number"));
            user.setEmail(resultSet.getString("email"));
            user.setCreated(resultSet.getTimestamp("created"));
            user.setUpdated(resultSet.getTimestamp("updated"));
            user.setSex(resultSet.getString("sex"));
            user.setDeleted(resultSet.getBoolean("is_deleted"));
            return Optional.of(user);
        }
        return Optional.empty();
    }
}