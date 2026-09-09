package kr.gsm.minibank.controller;

import jakarta.validation.Valid;
import kr.gsm.minibank.dto.TransferRequestDto;
import kr.gsm.minibank.dto.TransferResponseDto;
import kr.gsm.minibank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/transfer")
public class TransferController {
    private final TransferService transferService;

    @PostMapping()
    public ResponseEntity<TransferResponseDto> transfer(@Valid @RequestBody TransferRequestDto dto) {
        TransferResponseDto result = transferService.transfer(dto);
        return ResponseEntity.ok(result);
    }
}
