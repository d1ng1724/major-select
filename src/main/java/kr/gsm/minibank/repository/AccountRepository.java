package kr.gsm.minibank.repository;

import kr.gsm.minibank.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByCustomerId(Long customerId);
    Optional<Account> findByAccountNumber(String accountNumber);

    // n+1 문제 해결
    @Query("select a from Account a join fetch a.customer")
    List<Account> findAllWithCustomer();
}
