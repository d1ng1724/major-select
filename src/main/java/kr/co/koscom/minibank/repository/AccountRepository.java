package kr.co.koscom.minibank.repository;

import kr.co.koscom.minibank.domain.Account;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AccountRepository {

    private final Map<Long, Account> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);

    public Account save(Account account){
        if (account.getId() == null) {
            account.setId(sequence.incrementAndGet());
        }
        store.put(account.getId(), account);
        return account;
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Account> findAll() {
        return new ArrayList<>(store.values());
    }
}
