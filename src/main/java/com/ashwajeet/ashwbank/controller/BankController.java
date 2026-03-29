package com.ashwajeet.ashwbank.controller;

import com.ashwajeet.ashwbank.dto.*;
import com.ashwajeet.ashwbank.service.BankService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/profile")
    public ResponseEntity<AuthResponse> getProfile(
            Authentication auth) {
        return ResponseEntity.ok(
            bankService.getProfile(auth.getName()));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(
            @Valid @RequestBody TransactionRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
            bankService.deposit(request, auth.getName()));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(
            @Valid @RequestBody TransactionRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
            bankService.withdraw(request, auth.getName()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(
            @Valid @RequestBody TransactionRequest request,
            Authentication auth) {
        return ResponseEntity.ok(
            bankService.transfer(request, auth.getName()));
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionResponse>> getHistory(
            Authentication auth) {
        return ResponseEntity.ok(
            bankService.getHistory(auth.getName()));
    }
}

