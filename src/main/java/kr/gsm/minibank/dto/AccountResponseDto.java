package kr.gsm.minibank.dto;

import kr.gsm.minibank.domain.Account;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponseDto {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balance;
    private LocalDateTime openedAt;
    private BigDecimal dailyLimit;

    public static AccountResponseDto from(Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .ownerName(account.getCustomer().getName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .openedAt(account.getOpenedAt())
                .dailyLimit(account.getDailyLimit())
                .build();
    }
}
