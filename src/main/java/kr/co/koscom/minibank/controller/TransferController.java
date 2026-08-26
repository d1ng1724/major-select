package kr.co.koscom.minibank.controller;

import jakarta.validation.Valid;
import kr.co.koscom.minibank.dto.TransferRequestDto;
import kr.co.koscom.minibank.dto.TransferResponseDto;
import kr.co.koscom.minibank.service.TransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {
    private final TransferService transferService;

    @PostMapping()
    public ResponseEntity<TransferResponseDto> transfer(@Valid @RequestBody TransferRequestDto dto) {
        TransferResponseDto result = transferService.transfer(dto);
        return ResponseEntity.ok(result);
    }
}
