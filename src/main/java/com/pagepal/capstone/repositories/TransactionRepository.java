package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Transaction;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByTransactionTypeAndStatus(TransactionTypeEnum transactionType, TransactionStatusEnum status);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.transactionType = :transactionType
            AND t.status = :status
            """)
    List<Transaction> findByTransactionTypeStringAndStatusString(TransactionTypeEnum transactionType, TransactionStatusEnum status);
}