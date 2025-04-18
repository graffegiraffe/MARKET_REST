package com.tms.security;

import com.tms.model.Security;
import com.tms.repository.SecurityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final SecurityRepository securityRepository;

    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailService.class);

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Security security = securityRepository.findByLogin(username)
                .orElseThrow(() -> {
                    log.error("User not found: {}", username);
                    throw new UsernameNotFoundException("User not found");
                });

        log.info("User found: {}, role: {}", username, security.getRole());
        return User
                .withUsername(security.getLogin())
                .password(security.getPassword())
                .roles(security.getRole().toString())
                .build();
    }
}