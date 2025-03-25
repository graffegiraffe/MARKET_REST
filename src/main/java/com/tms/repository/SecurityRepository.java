package com.tms.repository;

import com.tms.config.DatabaseService;
import com.tms.config.SQLQuery;
import com.tms.model.Role;
import com.tms.model.dto.RegistrationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class SecurityRepository {

    private DatabaseService databaseService;

    @Autowired
    public SecurityRepository(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Boolean registration(RegistrationRequestDto requestDto) throws SQLException {
        Connection connection = databaseService.getConnection();

        try {
            connection.setAutoCommit(false);
            PreparedStatement createUserStatement = connection.prepareStatement(SQLQuery.CREATE_USER, Statement.RETURN_GENERATED_KEYS);
            createUserStatement.setString(1, requestDto.getFirstname());
            createUserStatement.setString(2, requestDto.getSecondName());
            createUserStatement.setInt(3, requestDto.getAge());
            createUserStatement.setString(4, requestDto.getTelephoneNumber());
            createUserStatement.setString(5, requestDto.getEmail());
            createUserStatement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            createUserStatement.setString(7, requestDto.getSex());
            createUserStatement.setBoolean(8, false);
            createUserStatement.executeUpdate();

            ResultSet generatedKeys = createUserStatement.getGeneratedKeys();
            long userId = -1;
            if (generatedKeys.next()) {
                userId = generatedKeys.getLong(1);
            }

            PreparedStatement createSecurityStatement = connection.prepareStatement(SQLQuery.CREATE_SECURITY);
            createSecurityStatement.setString(1, requestDto.getLogin());
            createSecurityStatement.setString(2, requestDto.getPassword());
            createSecurityStatement.setString(3, Role.USER.toString());
            createSecurityStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            createSecurityStatement.setLong(5, userId);
            createSecurityStatement.executeUpdate();

            connection.commit();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            connection.rollback();
        }
        return false;
    }

    public Boolean isLoginUsed(String login) throws SQLException {
        Connection connection = databaseService.getConnection();
        try {
            PreparedStatement createUserStatement = connection.prepareStatement(SQLQuery.GET_SECURITY_BY_LOGIN);
            createUserStatement.setString(1, login);
            ResultSet resultSet = createUserStatement.executeQuery();
            while (resultSet.next()) {
                if (resultSet.getString("login").equals(login)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }
}