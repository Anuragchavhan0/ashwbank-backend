package com.ashwajeet.ashwbank.dto;

import com.ashwajeet.ashwbank.model.User;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password required")
    @Size(min = 6, message = "Password min 6 characters")
    private String password;

    private User.AccountType accountType;
}
