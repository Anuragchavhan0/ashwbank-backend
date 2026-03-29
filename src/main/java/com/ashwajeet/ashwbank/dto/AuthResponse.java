
package com.ashwajeet.ashwbank.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String name;
    private String email;
    private String accountNumber;
    private Double balance;
    private String accountType;
    private String message;
}