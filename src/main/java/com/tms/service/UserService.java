package com.tms.service;

import com.tms.model.Security;
import com.tms.model.User;
import com.tms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> updateUser(User user) {
        if (user.getId() == null || !userRepository.existsById(user.getId())) {
            throw new IllegalArgumentException("User ID is missing or does not exist");
        }

        if (user.getSecurityInfo() != null) {
            Security security = user.getSecurityInfo();
            if (security.getLogin() == null || security.getLogin().length() < 4 || security.getLogin().length() > 50) {
                throw new IllegalArgumentException("Login must be between 4 and 50 characters");
            }
            if (security.getPassword() == null || security.getPassword().length() < 6 || security.getPassword().length() > 100) {
                throw new IllegalArgumentException("Password must be between 6 and 100 characters");
            }
        }

        try {
            User updatedUser = userRepository.save(user);
            return Optional.of(updatedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<User> createUser(User user) {
        User savedUser = userRepository.save(user);
        return Optional.of(savedUser);
    }
}