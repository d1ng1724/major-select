package kr.co.koscom.minibank.dto;

import jakarta.validation.constraints.NotNull;
import kr.co.koscom.minibank.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDto {
    private String name;
    private String phone;

    public static CustomerResponseDto from(Customer customer) {
        return CustomerResponseDto.builder()
                .name(customer.getName())
                .phone(customer.getPhone())
                .build();
    }
}
