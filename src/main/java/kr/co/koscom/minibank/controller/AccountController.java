package kr.co.koscom.minibank.controller;

import jakarta.validation.Valid;
import kr.co.koscom.minibank.dto.AccountCreateRequestDto;
import kr.co.koscom.minibank.dto.AccountResponseDto;
import kr.co.koscom.minibank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("")
    public ResponseEntity<AccountResponseDto> createAccount(@Valid @RequestBody AccountCreateRequestDto accountCreateRequestDto) {
        AccountResponseDto dto = accountService.createAccount(accountCreateRequestDto);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("")
    public ResponseEntity<List<AccountResponseDto>> getAll() {
        List<AccountResponseDto> dtos = accountService.getAll();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDto> getAccountById(@PathVariable Long id) {
        AccountResponseDto dto = accountService.getById(id);
        return ResponseEntity.ok(dto);
    }
}
