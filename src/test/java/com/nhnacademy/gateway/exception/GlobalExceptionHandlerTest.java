package com.nhnacademy.gateway.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.gateway.dto.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private HttpServletRequest request;

    @Mock
    private Model model;

    @Test
    void testHandleHttpClientErrorException_Success() throws Exception {
        String json = "{\"status\":404, \"message\":\"Not Found\", \"path\":\"/test\"}";
        ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "/test");
        HttpStatusCodeException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found", json.getBytes(), null);

        when(objectMapper.readValue(any(String.class), eq(ErrorResponse.class))).thenReturn(errorResponse);

        String view = globalExceptionHandler.handleHttpClientErrorException(ex, request, model);

        assertEquals("error", view);
        verify(model).addAttribute(eq("error"), eq(errorResponse));
    }

    @Test
    void testHandleHttpClientErrorException_ParsingFailure() throws Exception {
        HttpStatusCodeException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found");
        when(objectMapper.readValue(any(String.class), eq(ErrorResponse.class))).thenThrow(new RuntimeException("Parsing error"));
        when(request.getRequestURI()).thenReturn("/test");

        String view = globalExceptionHandler.handleHttpClientErrorException(ex, request, model);

        assertEquals("error", view);
        // HttpClientErrorException message format is typically "404 Not Found"
        verify(model).addAttribute(eq("error"), any(ErrorResponse.class));
    }

    @Test
    void testHandleGeneralException() {
        Exception ex = new RuntimeException("Unexpected error");
        when(request.getRequestURI()).thenReturn("/test");

        String view = globalExceptionHandler.handleGeneralException(ex, request, model);

        assertEquals("error", view);
        verify(model).addAttribute(eq("error"), any(ErrorResponse.class));
    }
}
