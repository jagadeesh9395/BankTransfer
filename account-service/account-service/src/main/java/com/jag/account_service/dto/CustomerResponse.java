package com.jag.account_service.dto;

import com.jag.account_service.entity.sql.Customer;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record CustomerResponse(
    String customerId,
    String name,
    String email,
    String phoneNumber,
    String aadhaarNumber,
    String panNumber,
    LocalDate dateOfBirth,
    Boolean kycVerified,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static CustomerResponse from(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getAadhaarNumber(),
                customer.getPanNumber(),
                customer.getDateOfBirth(),
                customer.getKycVerified(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}
