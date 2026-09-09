package kr.gsm.minibank.dto;

import kr.gsm.minibank.domain.TransactionHistory;
import kr.gsm.minibank.domain.TransactionType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    private LocalDateTime transactionAt;

    public static TransactionHistoryResponseDto from(TransactionHistory transactionHistory) {
        return TransactionHistoryResponseDto.builder()
                .id(transactionHistory.getId())
                .ownerName(transactionHistory.getAccount().getCustomer().getName())
                .accountNumber(transactionHistory.getAccount().getAccountNumber())
                .balanceAfter(transactionHistory.getBalanceAfter())
                .amount(transactionHistory.getAmount())
                .type(transactionHistory.getType())
                .transactionAt(transactionHistory.getTransactedAt())
                .build();
    }
}
