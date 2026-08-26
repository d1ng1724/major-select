package kr.co.koscom.minibank.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerCreateDto {
    @NotNull(message = "이름을 입력하세요")
    private String name;
    private String phone;
}
