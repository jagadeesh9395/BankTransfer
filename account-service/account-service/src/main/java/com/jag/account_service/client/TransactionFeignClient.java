package com.jag.account_service.client;

import com.jag.account_service.dto.TransactionRequest;
import com.jag.account_service.dto.TransactionResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "transaction-service"
)
public interface TransactionFeignClient {

    @PostMapping("/api/v1/transactions")
    TransactionResponse createTransaction(@RequestBody TransactionRequest request);

    @GetMapping("/api/v1/transactions")
    List<TransactionResponse> getTransactionsByAccount(@RequestParam("accountNumber") String accountNumber);
}