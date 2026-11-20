package com.jag.account_service.entity.sql;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_logins")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLogin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String customerId;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private Boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;
}
