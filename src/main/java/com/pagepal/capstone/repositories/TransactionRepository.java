package com.pagepal.capstone.repositories;

import com.pagepal.capstone.entities.postgre.Transaction;
import com.pagepal.capstone.enums.TransactionStatusEnum;
import com.pagepal.capstone.enums.TransactionTypeEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
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

    Page<Transaction> findByCreateAtBetween(Date startDate, Date endDate, Pageable pageable);

    Page<Transaction> findByCreateAtBetweenAndTransactionType(Date startDate, Date endDate,
                                                              TransactionTypeEnum transactionType,
                                                              Pageable pageable);

    Page<Transaction> findByTransactionType(TransactionTypeEnum transactionType, Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.wallet.account.customer.id = :customerId
            AND t.transactionType IN :transactionTypes
            """)
    Page<Transaction> findByCustomerIdAndTransactionTypes(UUID customerId,
                                                          List<TransactionTypeEnum> transactionTypes,
                                                          Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.wallet.account.reader.id = :readerId
            AND t.transactionType IN :transactionTypes
            """)
    Page<Transaction> findByReaderIdAndTransactionTypes(UUID readerId,
                                                        List<TransactionTypeEnum> transactionTypes,
                                                        Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.wallet.account.customer.id = :customerId
            AND t.transactionType = :transactionType
            """)
    Page<Transaction> findByCustomerIdAndTransactionType(UUID customerId,
                                                         TransactionTypeEnum transactionType,
                                                         Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.wallet.account.reader.id = :readerId
            AND t.transactionType = :transactionType
            """)
    Page<Transaction> findByReaderIdAndTransactionType(UUID readerId,
                                                       TransactionTypeEnum transactionType,
                                                       Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.createAt BETWEEN :startDate AND :endDate
            AND t.wallet.account.customer.id = :customerId
            AND t.transactionType IN :transactionTypes
            """)
    Page<Transaction> findByCreateAtBetweenAndCustomerIdAndTransactionTypes(Date startDate, Date endDate,
                                                                            UUID customerId,
                                                                            List<TransactionTypeEnum> transactionTypes,
                                                                            Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.createAt BETWEEN :startDate AND :endDate
            AND t.wallet.account.reader.id = :customerId
            AND t.transactionType IN :transactionTypes
            """)
    Page<Transaction> findByCreateAtBetweenAndReaderIdAndTransactionTypes(Date startDate, Date endDate,
                                                                          UUID customerId,
                                                                          List<TransactionTypeEnum> transactionTypes,
                                                                          Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.createAt BETWEEN :startDate AND :endDate
            AND t.wallet.account.customer.id = :customerId
            AND t.transactionType = :transactionType
            """)
    Page<Transaction> findByCreateAtBetweenAndCustomerIdAndTransactionType(Date startDate, Date endDate,
                                                                           UUID customerId,
                                                                           TransactionTypeEnum transactionType,
                                                                           Pageable pageable);

    @Query("""
            SELECT t
            FROM Transaction t
            WHERE t.createAt BETWEEN :startDate AND :endDate
            AND t.wallet.account.reader.id = :readerId
            AND t.transactionType = :transactionType
            """)
    Page<Transaction> findByCreateAtBetweenAndReaderIdAndTransactionType(Date startDate, Date endDate,
                                                                           UUID readerId,
                                                                           TransactionTypeEnum transactionType,
                                                                           Pageable pageable);

}