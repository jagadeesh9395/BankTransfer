package com.jag.account_service.repository.sql;

import com.jag.account_service.entity.sql.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String fromAccountNum);

    //GetAccountBalanceByAccountNumber
    @Query("SELECT a.balance FROM Account a WHERE a.accountNumber = :accountNumber")
    Double getAccountBalanceByAccountNumber(String accountNumber);
}
