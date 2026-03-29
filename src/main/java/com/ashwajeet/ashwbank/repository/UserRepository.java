
package com.ashwajeet.ashwbank.repository;

import com.ashwajeet.ashwbank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
}