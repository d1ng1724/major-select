package kr.co.koscom.minibank.service;

import kr.co.koscom.minibank.domain.Account;
import kr.co.koscom.minibank.domain.Customer;
import kr.co.koscom.minibank.dto.AccountCreateRequestDto;
import kr.co.koscom.minibank.dto.AccountResponseDto;
import kr.co.koscom.minibank.exception.NotFoundException;
import kr.co.koscom.minibank.repository.AccountRepository;
import kr.co.koscom.minibank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;

    @Transactional
    public AccountResponseDto createAccount(AccountCreateRequestDto accountCreateRequestDto) {
        log.info("계좌 개설 요청 ( 고객 Id: {}, 잔액: {} )", accountCreateRequestDto.getCustomerId(), accountCreateRequestDto.getInitialBalance());

        /* 고객  */
        Customer customer = customerRepository.findById(accountCreateRequestDto.getCustomerId()).orElseThrow(NotFoundException::new);


        Account account = Account.builder()
                .customer(customer)
                .accountNumber(generateAccountNumber())
                .balance(accountCreateRequestDto.getInitialBalance())
                .openedAt(LocalDateTime.now())
                .build();
        customer.addAccount(account);


        Account savedAccount = accountRepository.save(account);
        return AccountResponseDto.from(savedAccount);
    }

    public List<AccountResponseDto> getAll() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountResponseDto::from).toList();
    }

    public AccountResponseDto getById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(NotFoundException::new);
        return AccountResponseDto.from(account);
    }

    public List<AccountResponseDto> getByCustomerId(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.stream().map(AccountResponseDto::from).toList();
    }

    public AccountResponseDto getByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(NotFoundException::new);
        return AccountResponseDto.from(account);
    }

    private String generateAccountNumber() {
        return "110-" + (1000 + new Random().nextInt(9000))
                + "-" + (1000 + new Random().nextInt(9000));
    }
}
