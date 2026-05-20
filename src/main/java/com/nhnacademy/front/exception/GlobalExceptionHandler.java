package com.nhnacademy.front.exception;

import com.nhnacademy.front.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper;

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFoundException(NoResourceFoundException ex) {
        // favicon.ico 등 누락된 리소스 요청에 대해 로그를 남기지 않고 404 반환
        return ResponseEntity.notFound().build();
    }

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

    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public String handleValidationException(Exception ex, HttpServletRequest request, Model model) {
        log.error("유효성 검사 실패: {}", ex.getMessage());
        
        String message;
        if (ex instanceof BindException bindException) {
            message = bindException.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else if (ex instanceof MethodArgumentNotValidException methodArgumentNotValidException) {
            message = methodArgumentNotValidException.getBindingResult().getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
        } else {
            message = "유효성 검사 오류가 발생했습니다.";
        }

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                request.getRequestURI()
        );
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
