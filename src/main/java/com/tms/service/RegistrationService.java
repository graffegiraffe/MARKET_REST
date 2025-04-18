package com.tms.service;

import com.tms.model.Role;
import com.tms.model.Security;
import com.tms.model.User;
import com.tms.model.dto.RegistrationRequestDto;
import com.tms.repository.SecurityRepository;
import com.tms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final SecurityRepository securityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationService(UserRepository userRepository,
                               SecurityRepository securityRepository,
                               PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.securityRepository = securityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public boolean register(RegistrationRequestDto requestDto) {
        // Создаем User
        User user = new User();
        user.setFirstname(requestDto.getFirstname());
        user.setSecondName(requestDto.getSecondName());
        user.setAge(requestDto.getAge());
        user.setEmail(requestDto.getEmail());
        user.setSex(requestDto.getSex());
        user.setTelephoneNumber(requestDto.getTelephoneNumber());
        user.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
        user.setUpdated(new java.sql.Timestamp(System.currentTimeMillis()));
        user.setIsDeleted(false);
        user = userRepository.save(user);

        // Создаем Security
        Security security = new Security();
        security.setLogin(requestDto.getLogin());
        security.setPassword(passwordEncoder.encode(requestDto.getPassword())); // Шифруем пароль
        security.setRole(Role.USER);
        security.setCreated(new java.sql.Timestamp(System.currentTimeMillis()));
        security.setUpdated(new java.sql.Timestamp(System.currentTimeMillis()));
        security.setUser(user);
        securityRepository.save(security);

        user.setSecurityInfo(security);
        return true;
    }
}