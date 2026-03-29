package com.ashwajeet.ashwbank.service;

import com.ashwajeet.ashwbank.dto.*;
import com.ashwajeet.ashwbank.model.*;
import com.ashwajeet.ashwbank.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found!"));
    }

    public AuthResponse getProfile(String email) {
        User user = getUser(email);
        return AuthResponse.builder()
            .name(user.getName())
            .email(user.getEmail())
            .accountNumber(user.getAccountNumber())
            .balance(user.getBalance())
            .accountType(user.getAccountType().name())
            .build();
    }

    @Transactional
    public TransactionResponse deposit(
            TransactionRequest request, String email) {

        User user = getUser(email);
        user.setBalance(user.getBalance() + request.getAmount());
        userRepository.save(user);

        Transaction tx = Transaction.builder()
            .user(user)
            .type(Transaction.TransactionType.DEPOSIT)
            .amount(request.getAmount())
            .balanceAfter(user.getBalance())
            .description(request.getDescription() != null
                ? request.getDescription() : "Deposit")
            .build();

        transactionRepository.save(tx);
        return mapToResponse(tx);
    }

    @Transactional
    public TransactionResponse withdraw(
            TransactionRequest request, String email) {

        User user = getUser(email);

        if (user.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance!");
        }

        user.setBalance(user.getBalance() - request.getAmount());
        userRepository.save(user);

        Transaction tx = Transaction.builder()
            .user(user)
            .type(Transaction.TransactionType.WITHDRAWAL)
            .amount(request.getAmount())
            .balanceAfter(user.getBalance())
            .description(request.getDescription() != null
                ? request.getDescription() : "Withdrawal")
            .build();

        transactionRepository.save(tx);
        return mapToResponse(tx);
    }

    @Transactional
    public TransactionResponse transfer(
            TransactionRequest request, String email) {

        User sender = getUser(email);

        if (sender.getBalance() < request.getAmount()) {
            throw new RuntimeException("Insufficient balance!");
        }

        User receiver = userRepository
            .findByAccountNumber(request.getTargetAccountNumber())
            .orElseThrow(() ->
                new RuntimeException("Target account not found!"));

        if (sender.getId().equals(receiver.getId())) {
            throw new RuntimeException(
                "Cannot transfer to your own account!");
        }

        sender.setBalance(sender.getBalance() - request.getAmount());
        receiver.setBalance(receiver.getBalance() + request.getAmount());
        userRepository.save(sender);
        userRepository.save(receiver);

        Transaction sentTx = Transaction.builder()
            .user(sender)
            .type(Transaction.TransactionType.TRANSFER_SENT)
            .amount(request.getAmount())
            .balanceAfter(sender.getBalance())
            .targetAccountNumber(request.getTargetAccountNumber())
            .description("Transfer to " + receiver.getName())
            .build();

        Transaction receivedTx = Transaction.builder()
            .user(receiver)
            .type(Transaction.TransactionType.TRANSFER_RECEIVED)
            .amount(request.getAmount())
            .balanceAfter(receiver.getBalance())
            .targetAccountNumber(sender.getAccountNumber())
            .description("Transfer from " + sender.getName())
            .build();

        transactionRepository.save(sentTx);
        transactionRepository.save(receivedTx);
        return mapToResponse(sentTx);
    }

    public List<TransactionResponse> getHistory(String email) {
        User user = getUser(email);
        return transactionRepository
            .findByUserOrderByCreatedAtDesc(user)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction tx) {
        return TransactionResponse.builder()
            .id(tx.getId())
            .type(tx.getType().name())
            .amount(tx.getAmount())
            .balanceAfter(tx.getBalanceAfter())
            .targetAccountNumber(tx.getTargetAccountNumber())
            .description(tx.getDescription())
            .createdAt(tx.getCreatedAt())
            .build();
    }
}
