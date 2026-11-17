package com.jag.account_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CustomerRequest(
    @NotBlank(message = "Customer ID is mandatory")
    @Size(max = 255, message = "Customer ID must not exceed 255 characters")
    String customerId,

    @NotBlank(message = "Customer name is mandatory")
    String name,

    @NotBlank(message = "Customer email is mandatory")
    @Email(message = "Customer email must be valid")
    String email,

    String phoneNumber,
    String aadhaarNumber,
    String panNumber,
    LocalDate dateOfBirth,
    Boolean kycVerified
) {}
