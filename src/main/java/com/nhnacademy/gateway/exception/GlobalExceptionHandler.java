package com.nhnacademy.gateway.exception;

import com.nhnacademy.gateway.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpStatusCodeException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(HttpStatusCodeException.class)
    public String handleHttpClientErrorException(HttpStatusCodeException ex, HttpServletRequest request, Model model) {
        log.error("API 호출 중 오류 발생: {}", ex.getMessage());
        ErrorResponse errorResponse;
        try {
            // API 서버에서 보낸 ErrorResponse JSON을 역직렬화
            errorResponse = objectMapper.readValue(ex.getResponseBodyAsString(), ErrorResponse.class);
        } catch (Exception e) {
            // JSON 파싱 실패 시 기본 정보로 ErrorResponse 생성
            log.warn("응답 바디 파싱 실패", e);
            errorResponse = new ErrorResponse(
                    ex.getStatusCode().value(),
                    ex.getMessage(),
                    request.getRequestURI()
            );
        }
        
        model.addAttribute("error", errorResponse);
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, HttpServletRequest request, Model model) {
        log.error("서버 내부 오류", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "내부 서버 오류가 발생했습니다: " + ex.getMessage(),
                request.getRequestURI()
        );
        model.addAttribute("error", errorResponse);
        return "error";
    }
}
