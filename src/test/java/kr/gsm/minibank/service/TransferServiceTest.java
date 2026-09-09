package kr.gsm.minibank.service;

import kr.gsm.minibank.domain.Account;
import kr.gsm.minibank.domain.Customer;
import kr.gsm.minibank.domain.TransactionHistory;
import kr.gsm.minibank.domain.TransactionType;
import kr.gsm.minibank.dto.TransferRequestDto;
import kr.gsm.minibank.exception.InsufficientBalanceException;
import kr.gsm.minibank.repository.AccountRepository;
import kr.gsm.minibank.repository.CustomerRepository;
import kr.gsm.minibank.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class) // 실제 스프링 서버를 띄우지 않고 가짜(Mock) 객체를 사용합니다.
class TransferServiceTest {

    @Mock
    private AccountRepository accountRepository; // 가짜 데이터베이스(Repository)
    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService; // 가짜 DB를 주입받은 테스트 대상 서비스

    @InjectMocks
    private TransferService transferService;

    @Test
    @DisplayName("계좌 이체 성공: 두 계좌의 잔액이 정확히 출금/입금된다")
    void 이체_성공시_두_계좌_잔액이_정확히_변경된다() {
        // given (테스트 준비 단계: 가짜 상황 설정)
        Customer fromCustomer = Customer.builder()
                .name("1")
                .phone("010-1111-1111")
                .build();
        Customer toCustomer = Customer.builder()
                .name("2")
                .phone("010-2222-2222")
                .build();
        Account from = Account.builder()
                .id(1L)
                .accountNumber("110-1111-1111")
                .balance(new BigDecimal("50000"))
                .customer(fromCustomer)
                .build();
        Account to = Account.builder()
                .id(2L)
                .accountNumber("110-2222-2222")
                .balance(new BigDecimal("0"))
                .customer(toCustomer)
                .build();

        fromCustomer.addAccount(from);
        toCustomer.addAccount(to);

        TransactionHistory fromHistory = TransactionHistory.builder()
                .id(1L)
                .account(from)
                .amount(new BigDecimal(20000))
                .type(TransactionType.WITHDRAW)
                .build();
        TransactionHistory toHistory = TransactionHistory.builder()
                .id(1L)
                .account(to)
                .amount(new BigDecimal(20000))
                .type(TransactionType.DEPOSIT)
                .build();

        given(accountRepository.findByAccountNumber("110-1111-1111")).willReturn(Optional.of(from));
        given(accountRepository.findByAccountNumber("110-2222-2222")).willReturn(Optional.of(to));
        // 일일 한도 검사(validateDailyLimit)가 출금 계좌를 다시 조회합니다.
        given(accountRepository.findById(1L)).willReturn(Optional.of(from));

        given(transactionHistoryRepository.save(any(TransactionHistory.class))).willReturn(fromHistory, toHistory);


        TransferRequestDto request = TransferRequestDto.builder()
                .fromAccountNumber("110-1111-1111")
                .toAccountNumber("110-2222-2222")
                .amount(new BigDecimal(20000))
                .build();
        // when (테스트 실행)
        transferService.transfer(request);

        // then (결과 검증: 5만원-2만원=3만원 / 0원+2만원=2만원)
        assertThat(from.getBalance()).isEqualByComparingTo("30000");
        assertThat(to.getBalance()).isEqualByComparingTo("20000");
    }

    @Test
    @DisplayName("계좌 이체 실패: 잔액보다 큰 금액을 이체하면 예외가 터져야 한다")
    void 잔액이_부족하면_InsufficientBalanceException이_발생한다() {
        // given (출금 계좌에 1,000원밖에 없는 상황)
        Account from = Account.builder()
                .id(1L)
                .accountNumber("110-1111-1111")
                .balance(new BigDecimal("1000"))
                .build();
        Account to = Account.builder()
                .id(2L)
                .accountNumber("110-2222-2222")
                .balance(new BigDecimal("0"))
                .build();

        given(accountRepository.findByAccountNumber("110-1111-1111")).willReturn(Optional.of(from));
        given(accountRepository.findByAccountNumber("110-2222-2222")).willReturn(Optional.of(to));
        // 일일 한도 검사(validateDailyLimit)가 출금 계좌를 다시 조회합니다.
        given(accountRepository.findById(1L)).willReturn(Optional.of(from));

        TransferRequestDto request = new TransferRequestDto();
        request.setFromAccountNumber("110-1111-1111");
        request.setToAccountNumber("110-2222-2222");
        request.setAmount(new BigDecimal("999999")); // 100만 원 이체 시도!

        // when & then (예외가 정상적으로 터지는 것을 검증)
        assertThatThrownBy(() -> transferService.transfer(request))
                .isInstanceOf(InsufficientBalanceException.class)
                .hasMessageContaining("잔액이 부족합니다");
    }
}
