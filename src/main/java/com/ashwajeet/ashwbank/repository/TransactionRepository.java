package com.ashwajeet.ashwbank.repository;

import com.ashwajeet.ashwbank.model.Transaction;
import com.ashwajeet.ashwbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserOrderByCreatedAtDesc(User user);
}

