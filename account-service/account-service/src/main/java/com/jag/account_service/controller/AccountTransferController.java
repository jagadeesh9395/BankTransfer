package com.jag.account_service.controller;

import com.jag.account_service.dto.TransactionResponse;
import com.jag.account_service.dto.TransferRequest;
import com.jag.account_service.service.AccountTransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/accounts/transfers")
@RequiredArgsConstructor
public class AccountTransferController {

    private final AccountTransferService accountTransferService;

    @PostMapping
    public ResponseEntity<TransactionResponse> initiateTransfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = accountTransferService.initiateTransfer(
                request.fromAccountNumber(),
                request.toAccountNumber(),
                request.amount(),
                request.performedBy()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
