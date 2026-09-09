package kr.gsm.minibank.service;

import kr.gsm.minibank.domain.Account;
import kr.gsm.minibank.domain.TransactionHistory;
import kr.gsm.minibank.domain.TransactionType;
import kr.gsm.minibank.dto.TransactionHistoryResponseDto;
import kr.gsm.minibank.dto.TransferRequestDto;
import kr.gsm.minibank.dto.TransferResponseDto;
import kr.gsm.minibank.exception.DailyLimitExceededException;
import kr.gsm.minibank.exception.NotFoundException;
import kr.gsm.minibank.repository.AccountRepository;
import kr.gsm.minibank.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

        validateDailyLimit(fromAccount.getId(), dto.getAmount());

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
    
    @Transactional(readOnly = true)
    public List<TransactionHistoryResponseDto> getHistoriesByAccountId(Long id) {
        List<TransactionHistory> histories = transactionHistoryRepository.findByAccountIdOrderByTransactedAtDesc(id);
        return histories.stream().map(TransactionHistoryResponseDto::from).toList();
    }

    public BigDecimal getUsedAmount(Long accountId) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        return transactionHistoryRepository
                .findByAccountIdAndTypeAndTransactedAtBetween(accountId, TransactionType.WITHDRAW, startOfDay, endOfDay)
                .stream()
                .map(TransactionHistory::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateDailyLimit(Long accountId, BigDecimal amount) {
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);

        Account account = accountRepository.findById(accountId).orElseThrow(NotFoundException::new);

        BigDecimal usedToday = transactionHistoryRepository
                .findByAccountIdAndTypeAndTransactedAtBetween(accountId, TransactionType.WITHDRAW, startOfDay, endOfDay)
                .stream()
                .map(TransactionHistory::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal afterThisTransfer = usedToday.add(amount);
        if (afterThisTransfer.compareTo(account.getDailyLimit()) > 0) {
            throw new DailyLimitExceededException(account.getAccountNumber(),
                    account.getDailyLimit().toString(), usedToday.toString());
        }
    }
}
