package kr.co.koscom.minibank.dto;

import kr.co.koscom.minibank.domain.Account;
import kr.co.koscom.minibank.domain.TransactionHistory;
import kr.co.koscom.minibank.domain.TransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionHistoryResponseDto {
    private Long id;
    private String accountNumber;
    private String ownerName;
    private BigDecimal balanceAfter;
    private BigDecimal amount;
    private TransactionType type;

    public static TransactionHistoryResponseDto from(TransactionHistory transactionHistory) {
        return TransactionHistoryResponseDto.builder()
                .id(transactionHistory.getId())
                .ownerName(transactionHistory.getAccount().getCustomer().getName())
                .accountNumber(transactionHistory.getAccount().getAccountNumber())
                .balanceAfter(transactionHistory.getBalanceAfter())
                .amount(transactionHistory.getAmount())
                .type(transactionHistory.getType())
                .build();
    }
}
