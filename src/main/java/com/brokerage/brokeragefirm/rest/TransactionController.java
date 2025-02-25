package com.brokerage.brokeragefirm.rest;

import com.brokerage.brokeragefirm.common.constants.Constants;
import com.brokerage.brokeragefirm.common.mapper.AssetResponseMapper;
import com.brokerage.brokeragefirm.common.security.CustomUserDetails;
import com.brokerage.brokeragefirm.rest.dto.AssetResponse;
import com.brokerage.brokeragefirm.rest.dto.TransactionRequest;
import com.brokerage.brokeragefirm.service.TransactionService;
import com.brokerage.brokeragefirm.service.model.Transaction;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/deposit")
    public ResponseEntity<AssetResponse> deposit(@AuthenticationPrincipal CustomUserDetails loggedUser, @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = new Transaction(loggedUser.getCustomer().getId(), Constants.ASSET_TRY, request.amount());
        return ResponseEntity.ok(AssetResponseMapper.toResponse(transactionService.deposit(transaction)));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<AssetResponse> withdraw(@AuthenticationPrincipal CustomUserDetails loggedUser, @Valid @RequestBody TransactionRequest request) {
        Transaction transaction = new Transaction(loggedUser.getCustomer().getId(), Constants.ASSET_TRY, request.amount());
        return ResponseEntity.ok(AssetResponseMapper.toResponse(transactionService.withdraw(transaction)));
    }
}