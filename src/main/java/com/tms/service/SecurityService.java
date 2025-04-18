package com.tms.service;

import com.tms.model.Security;
import com.tms.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
    private final SecurityRepository securityRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityService(SecurityRepository securityRepository, PasswordEncoder passwordEncoder) {
        this.securityRepository = securityRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isLoginUsed(String login) {
        return securityRepository.findByLogin(login).isPresent();
    }


}