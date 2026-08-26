package kr.co.koscom.minibank.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequestDto {
    @NotBlank(message = "출금 계좌번호를 입력해주세요")
    private String fromAccountNumber;

    @NotBlank(message = "입금 계좌번호는 입력해주세요")
    private String toAccountNumber;

    @NotNull(message = "이체 금액을 입력해주세요")
    @Positive(message = "이체 금액은 0원보다 커야 합니다.")
    private BigDecimal amount;
}
