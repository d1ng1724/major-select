package kr.co.koscom.minibank.exception;


public class NotFoundException extends RuntimeException{
    public NotFoundException() {
        super("찾을 수 없습니다");
    }
}
