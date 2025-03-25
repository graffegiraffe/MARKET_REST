package com.tms.service;

import com.tms.model.dto.RegistrationRequestDto;
import com.tms.repository.SecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class SecurityService {

    public final SecurityRepository securityRepository;

    @Autowired
    public SecurityService(SecurityRepository securityRepository) {
        this.securityRepository = securityRepository;
    }

    public Boolean registration(RegistrationRequestDto requestDto) {
        try {
            if (securityRepository.isLoginUsed(requestDto.getLogin())){
                return false;
            }
            return securityRepository.registration(requestDto);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}