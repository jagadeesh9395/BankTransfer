package com.jag.account_service.repository.sql;

import com.jag.account_service.entity.sql.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
