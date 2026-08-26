package kr.co.koscom.minibank.dto;

import kr.co.koscom.minibank.domain.Account;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountBalanceDetailDto {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;

    public static AccountBalanceDetailDto from(Account account) {
        return AccountBalanceDetailDto.builder()
                .id(account.getId())
                .ownerName(account.getCustomer().getName())
                .accountNumber(account.getAccountNumber())
                .build();
    }
}
