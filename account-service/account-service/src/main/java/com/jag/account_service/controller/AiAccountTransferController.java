package com.jag.account_service.controller;


import com.jag.account_service.service.AiAccountServiceAgent;
import com.jag.account_service.util.AccountCustomerUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiAccountTransferController {

    private final AiAccountServiceAgent aiAccountServiceAgent;
    private final AccountCustomerUtil accountCustomerUtil;

    @GetMapping("/chat")
    public String chat(@RequestParam String message, @RequestParam(required = false) String username) {
        if (username == null || username.isBlank()) {
            username = accountCustomerUtil.getAuthenticatedUsername();
        }
        if (username == null) {
            username = "anonymous";
        }

        String customerName = accountCustomerUtil.getCustomerNameByUsername(username);
        String accountNumber = accountCustomerUtil.getAccountNumberByUsername(username);

        if (customerName == null) {
            customerName = "Guest";
        }
        if (accountNumber == null) {
            accountNumber = "N/A";
        }

        // Use username as memory ID for chat history
        return aiAccountServiceAgent.chat(username, customerName, accountNumber, message);
    }
}
