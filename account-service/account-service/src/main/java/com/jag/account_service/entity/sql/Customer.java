package com.jag.account_service.entity.sql;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String aadhaarNumber;
    private String panNumber;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private Boolean kycVerified;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
