package com.jag.account_service.service;

import com.jag.account_service.dto.CustomerRequest;
import com.jag.account_service.dto.CustomerResponse;
import com.jag.account_service.entity.sql.Customer;
import com.jag.account_service.repository.sql.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        if (customerRepository.existsById(request.customerId())) {
            throw new IllegalArgumentException("Customer already exists: " + request.customerId());
        }

        LocalDateTime now = LocalDateTime.now();
        Customer customer = Customer.builder()
                .id(request.customerId())
                .name(request.name())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .aadhaarNumber(request.aadhaarNumber())
                .panNumber(request.panNumber())
                .dateOfBirth(request.dateOfBirth())
                .kycVerified(request.kycVerified() != null ? request.kycVerified() : false)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return CustomerResponse.from(customerRepository.save(customer));
    }
}
