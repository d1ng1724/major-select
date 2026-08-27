package kr.co.koscom.minibank.controller;


import kr.co.koscom.minibank.dto.AccountCreateRequestDto;
import kr.co.koscom.minibank.dto.AccountResponseDto;
import kr.co.koscom.minibank.service.AccountService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class) // Controller 계층(API 웹 환경)만 가볍게 띄워 테스트합니다.
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc; // 가짜 HTTP 요청을 보내주는 도구

    @Autowired
    private JsonMapper objectMapper; // Java 객체 ↔ JSON 문자열 변환 (Jackson 3)

    @MockitoBean
    private AccountService accountService; // 실제 DB까지 가지 않도록 Service를 가짜(Mock)로 대체

    @Test
    @DisplayName("계좌 개설 API: 정상 요청 시 상태코드 201과 계좌번호가 반환된다")
    void 계좌_개설_성공시_201과_계좌번호를_반환한다() throws Exception {
        AccountCreateRequestDto request = new AccountCreateRequestDto();
        request.setCustomerId(1L);
        request.setInitialBalance(new BigDecimal("10000"));

        AccountResponseDto response = new AccountResponseDto();
        response.setId(1L);
        response.setAccountNumber("110-1234-5678");

        given(accountService.createAccount(any())).willReturn(response);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("110-1234-5678"));
    }

    @Test
    @DisplayName("계좌 개설 API: 고객 ID(필수값) 없이 요청하면 상태코드 400 에러를 뱉는다")
    void 고객ID_없이_요청하면_400을_반환한다() throws Exception {
        String invalidJson = "{ \"initialBalance\": 1000 }"; // customerId 누락

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
