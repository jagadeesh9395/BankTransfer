package com.jag.account_service.service;

import com.jag.account_service.dto.UserLoginRegisterRequest;
import com.jag.account_service.dto.UserLoginRequest;
import com.jag.account_service.dto.UserLoginResponse;
import com.jag.account_service.entity.sql.UserLogin;
import com.jag.account_service.repository.sql.UserLoginRepository;
import com.jag.account_service.repository.sql.CustomerRepository;
import com.jag.account_service.repository.sql.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserLoginService {

    private final UserLoginRepository userLoginRepository;
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserLoginResponse register(UserLoginRegisterRequest request) {
        if (userLoginRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists: " + request.username());
        }

        if (!customerRepository.existsById(request.customerId())) {
            throw new IllegalArgumentException("Customer not found: " + request.customerId());
        }

        if (accountRepository.findByAccountNumber(request.accountNumber()).isEmpty()) {
            throw new IllegalArgumentException("Account not found: " + request.accountNumber());
        }

        LocalDateTime now = LocalDateTime.now();
        UserLogin userLogin = UserLogin.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .customerId(request.customerId())
                .accountNumber(request.accountNumber())
                .active(true)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return UserLoginResponse.from(userLoginRepository.save(userLogin));
    }

    @Transactional
    public UserLoginResponse authenticate(UserLoginRequest request) {
        UserLogin userLogin = userLoginRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (!userLogin.getActive()) {
            throw new IllegalStateException("Account is disabled");
        }

        if (!passwordEncoder.matches(request.password(), userLogin.getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        userLogin.setLastLoginAt(LocalDateTime.now());
        userLogin.setUpdatedAt(LocalDateTime.now());
        userLoginRepository.save(userLogin);

        return UserLoginResponse.from(userLogin);
    }

    public UserLoginResponse getByUsername(String username) {
        return userLoginRepository.findByUsername(username)
                .map(UserLoginResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    public UserLoginResponse getByCustomerId(String customerId) {
        return userLoginRepository.findByCustomerId(customerId)
                .map(UserLoginResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("User not found for customer: " + customerId));
    }
}
