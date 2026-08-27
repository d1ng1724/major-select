package kr.co.koscom.minibank.domain;

import jakarta.persistence.*;
import kr.co.koscom.minibank.exception.InsufficientBalanceException;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;

    @ManyToOne(fetch = FetchType.LAZY) // @ManyToOne, @OneToOne - Default FetchType.EAGER
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal balance;

    private LocalDateTime openedAt;

    @Version
    private Long version;

    @Column(nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal dailyLimit = new BigDecimal("3000000");

    void assignCustomer(Customer customer) {
        this.customer = customer;
    }

    public void withdraw(BigDecimal amount) {
        if (this.balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException(this.accountNumber);
        }
        this.balance = this.balance.subtract(amount);
    }

    public void deposit(BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }

    public void changeDailyLimit(BigDecimal newDailyLimit) {
        if (newDailyLimit.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("이체한도는 0보다 커야 합니다.");
        }
        this.dailyLimit = newDailyLimit;
    }
}
