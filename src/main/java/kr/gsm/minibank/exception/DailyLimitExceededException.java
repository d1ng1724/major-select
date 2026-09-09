package kr.gsm.minibank.exception;

public class DailyLimitExceededException extends RuntimeException {
    public DailyLimitExceededException(String accountNumber, String dailyLimit, String requestedAmount) {
        super(String.format("일일 이체한도를 초과하였습니다. 계좌번호: %s, 일일 이체한도: %s, 금일 사용액: %s",
                accountNumber, dailyLimit, requestedAmount));
    }
}
