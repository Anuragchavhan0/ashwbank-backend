package com.ashwajeet.ashwbank.service;

import com.ashwajeet.ashwbank.dto.*;
import com.ashwajeet.ashwbank.model.User;
import com.ashwajeet.ashwbank.repository.UserRepository;
import com.ashwajeet.ashwbank.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered!");
        }

        User user = User.builder()
            .name(request.getName())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .accountNumber(generateAccountNumber())
            .balance(0.0)
            .accountType(request.getAccountType() != null
                ? request.getAccountType()
                : User.AccountType.SAVINGS)
            .active(true)
            .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder()
            .token(token)
            .name(user.getName())
            .email(user.getEmail())
            .accountNumber(user.getAccountNumber())
            .balance(user.getBalance())
            .accountType(user.getAccountType().name())
            .message("Account created successfully!")
            .build();
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password!");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder()
            .token(token)
            .name(user.getName())
            .email(user.getEmail())
            .accountNumber(user.getAccountNumber())
            .balance(user.getBalance())
            .accountType(user.getAccountType().name())
            .message("Login successful!")
            .build();
    }

    private String generateAccountNumber() {
        String prefix = "ASHW";
        String digits = String.format("%08d", new Random().nextInt(100000000));
        String accountNumber = prefix + digits;
        while (userRepository.existsByAccountNumber(accountNumber)) {
            digits = String.format("%08d", new Random().nextInt(100000000));
            accountNumber = prefix + digits;
        }
        return accountNumber;
    }
}

