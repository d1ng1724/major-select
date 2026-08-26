package kr.co.koscom.minibank.service;

import kr.co.koscom.minibank.domain.Account;
import kr.co.koscom.minibank.domain.TransactionHistory;
import kr.co.koscom.minibank.domain.TransactionType;
import kr.co.koscom.minibank.dto.TransactionHistoryResponseDto;
import kr.co.koscom.minibank.dto.TransferRequestDto;
import kr.co.koscom.minibank.dto.TransferResponseDto;
import kr.co.koscom.minibank.exception.NotFoundException;
import kr.co.koscom.minibank.repository.AccountRepository;
import kr.co.koscom.minibank.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransferService {
    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    @Transactional
    public TransferResponseDto transfer(TransferRequestDto dto) {

        Account fromAccount = accountRepository.findByAccountNumber(dto.getFromAccountNumber())
                .orElseThrow(NotFoundException::new);
        Account toAccount = accountRepository.findByAccountNumber(dto.getToAccountNumber())
                .orElseThrow(NotFoundException::new);

        fromAccount.withdraw(dto.getAmount());
        toAccount.deposit(dto.getAmount());

        TransactionHistory fromHistory = TransactionHistory.builder()
                .account(fromAccount)
                .type(TransactionType.WITHDRAW)
                .amount(dto.getAmount())
                .balanceAfter(fromAccount.getBalance())
                .transactedAt(LocalDateTime.now())
                .build();

        TransactionHistory toHistory = TransactionHistory.builder()
                .account(toAccount)
                .type(TransactionType.DEPOSIT)
                .amount(dto.getAmount())
                .balanceAfter(toAccount.getBalance())
                .transactedAt(LocalDateTime.now())
                .build();

        TransactionHistory savedFromHistory = transactionHistoryRepository.save(fromHistory);
        TransactionHistory savedToHistory = transactionHistoryRepository.save(toHistory);

        return TransferResponseDto.builder()
                .fromHistory(TransactionHistoryResponseDto.from(savedFromHistory))
                .toHistory(TransactionHistoryResponseDto.from(savedToHistory))
                .transferredAt(savedFromHistory.getTransactedAt())
                .build();
    }
}
