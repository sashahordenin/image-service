package com.example.imageservice.repository;

import com.example.imageservice.model.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("FROM Account a WHERE a.email = ?1")
    Optional<Account> findByEmail(String email);
}
