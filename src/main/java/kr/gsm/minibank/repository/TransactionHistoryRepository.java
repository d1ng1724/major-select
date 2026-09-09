package kr.gsm.minibank.repository;

import kr.gsm.minibank.domain.TransactionHistory;
import kr.gsm.minibank.domain.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    @Query("SELECT t FROM TransactionHistory t " +
            "JOIN FETCH t.account a " +
            "JOIN FETCH a.customer " +
            "WHERE a.id = :accountId " +
            "ORDER BY t.transactedAt DESC")
    List<TransactionHistory> findByAccountIdOrderByTransactedAtDesc(Long accountId);


    List<TransactionHistory> findByAccountIdAndTypeAndTransactedAtBetween(
            Long accountId, TransactionType type, LocalDateTime start, LocalDateTime end);
}
