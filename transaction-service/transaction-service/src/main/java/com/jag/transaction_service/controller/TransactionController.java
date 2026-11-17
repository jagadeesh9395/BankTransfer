package com.jag.transaction_service.controller;


import com.jag.transaction_service.dto.TransactionRequest;
import com.jag.transaction_service.dto.TransactionResponse;
import com.jag.transaction_service.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.processTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getTransactionsByAccount(
            @RequestParam("accountNumber") String accountNumber) {
        List<TransactionResponse> transactions = transactionService.getTransactions(accountNumber);
        return ResponseEntity.ok(transactions);
    }
}
