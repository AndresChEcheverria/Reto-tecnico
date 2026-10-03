package com.devsu.hackerearth.backend.account.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsu.hackerearth.backend.account.model.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Transaction findTopByAccountIdOrderByIdDesc(Long accountId);

    List<Transaction> findByAccountIdAndDateBetween(Long accountId, Date dateTransactionStart, Date dateTransactionEnd);

    List<Transaction> findByAccountId(Long accountId);
}

