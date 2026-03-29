package com.ashwajeet.ashwbank.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

// @Entity = this class maps to a database table
// @Table  = name of the table in MySQL
@Entity
@Table(name = "users")
@Data               // Lombok: generates getters + setters
@NoArgsConstructor  // Lombok: generates empty constructor
@AllArgsConstructor // Lombok: generates constructor with all fields
@Builder            // Lombok: lets you do User.builder().name("Ashw").build()
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private Double balance;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private boolean active = true;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        if (this.balance == null) this.balance = 0.0;
    }

    public enum AccountType {
        SAVINGS, CURRENT
    }
}
