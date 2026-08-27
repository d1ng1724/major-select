package kr.co.koscom.minibank.repository;

import kr.co.koscom.minibank.domain.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    @Query("SELECT t FROM TransactionHistory t " +
            "JOIN FETCH t.account a " +
            "JOIN FETCH a.customer " +
            "WHERE a.id = :accountId " +
            "ORDER BY t.transactedAt DESC")
    List<TransactionHistory> findByAccountIdOrderByTransactedAtDesc(Long accountId);
}
