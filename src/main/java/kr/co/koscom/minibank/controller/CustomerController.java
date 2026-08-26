package kr.co.koscom.minibank.controller;

import jakarta.validation.Valid;
import kr.co.koscom.minibank.dto.CustomerCreateDto;
import kr.co.koscom.minibank.dto.CustomerResponseDto;
import kr.co.koscom.minibank.dto.CustomerUpdateDto;
import kr.co.koscom.minibank.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("")
    public ResponseEntity<CustomerResponseDto> create(@Valid @RequestBody CustomerCreateDto dto) {
        CustomerResponseDto result = customerService.create(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("")
    public ResponseEntity<List<CustomerResponseDto>> getAll() {
        List<CustomerResponseDto> result = customerService.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        CustomerResponseDto result = customerService.getById(id);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> update(@PathVariable Long id, @RequestBody CustomerUpdateDto dto) {
        CustomerResponseDto result = customerService.update(id, dto);
        return ResponseEntity.ok(result);
    }
}
