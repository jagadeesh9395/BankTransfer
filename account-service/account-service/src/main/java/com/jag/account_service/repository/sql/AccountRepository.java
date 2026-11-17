package com.jag.account_service.repository.sql;

import com.jag.account_service.entity.sql.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account,Long> {
    Optional<Account> findByAccountNumber(String fromAccountNum);
}
