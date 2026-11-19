package com.jag.account_service.tool;

import com.jag.account_service.dto.TransactionResponse;
import com.jag.account_service.service.AccountTransferService;
import com.jag.account_service.util.AccountCustomerUtil;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountTransferTool {

    public AccountTransferService accountTransferService;

    public AccountTransferTool(AccountTransferService accountTransferService) {
        this.accountTransferService = accountTransferService;
    }

    @Tool("Get transactions by account number")
    public List<TransactionResponse> getTransactionByAccountNumber(String accountNumber) {
        System.out.println("Tool called -> getTransactionByAccountNumber: " + accountNumber);
        return accountTransferService.getTransactionByAccountNumber(accountNumber);
    }


    @Tool("Initiate transfer between accounts")
    public TransactionResponse initiateTransfer(String fromAccount, String toAccount, BigDecimal amount) {
        System.out.println("Tool called -> initiateTransfer: " + fromAccount + " -> " + toAccount + " : " + amount);
        String performedBy = AccountCustomerUtil.getPerformedBy(fromAccount);
        String customerName = AccountCustomerUtil.getCustomerName(performedBy);
        return accountTransferService.initiateTransfer(fromAccount, toAccount, amount, customerName);
    }

}
