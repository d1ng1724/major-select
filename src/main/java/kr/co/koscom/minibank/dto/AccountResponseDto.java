package kr.co.koscom.minibank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import kr.co.koscom.minibank.domain.Account;
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

    public static AccountResponseDto from(Account account) {
        return AccountResponseDto.builder()
                .id(account.getId())
                .ownerName(account.getCustomer().getName())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .openedAt(account.getOpenedAt())
                .build();
    }
}
