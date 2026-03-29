package com.ashwajeet.ashwbank.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TransactionRequest {

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private Double amount;

    private String targetAccountNumber;
    private String description;
}
