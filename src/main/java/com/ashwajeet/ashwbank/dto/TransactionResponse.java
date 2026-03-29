
package com.ashwajeet.ashwbank.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private String type;
    private Double amount;
    private Double balanceAfter;
    private String targetAccountNumber;
    private String description;
    private LocalDateTime createdAt;
}