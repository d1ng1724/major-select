package kr.co.koscom.minibank.repository;

import kr.co.koscom.minibank.domain.Account;
import kr.co.koscom.minibank.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
