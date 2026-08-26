package kr.co.koscom.minibank.service;

import kr.co.koscom.minibank.domain.Account;
import kr.co.koscom.minibank.dto.AccountBalanceDetailDto;
import kr.co.koscom.minibank.dto.TransferRequestDto;
import kr.co.koscom.minibank.dto.TransferResponseDto;
import kr.co.koscom.minibank.exception.NotFoundException;
import kr.co.koscom.minibank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;

    @Transactional
    public TransferResponseDto transfer(TransferRequestDto dto) {

        Account fromAccount = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
                .orElseThrow(NotFoundException::new);
        Account toAccount = accountRepository.findByAccountNumber(dto.getToAccountNumber())
                .orElseThrow(NotFoundException::new);

        AccountBalanceDetailDto fromBalance = AccountBalanceDetailDto.builder()
                .accountNumber(fromAccount.getAccountNumber())
                .balanceBefore(fromAccount.getBalance())
                .build();
        AccountBalanceDetailDto toBalance = AccountBalanceDetailDto.builder()
                .accountNumber(toAccount.getAccountNumber())
                .balanceBefore(toAccount.getBalance())
                .build();

        fromAccount.withdraw(dto.getAmount());
        toAccount.deposit(dto.getAmount());

        fromBalance.setBalanceAfter(fromAccount.getBalance());
        toBalance.setBalanceAfter(toAccount.getBalance());
        return TransferResponseDto.builder()
                .fromAccount(fromBalance)
                .toAccount(toBalance)
                .transferredAt(LocalDateTime.now())
                .build();
    }
}
