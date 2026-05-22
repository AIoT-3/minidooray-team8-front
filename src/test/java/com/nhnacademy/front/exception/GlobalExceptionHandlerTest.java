package com.nhnacademy.front.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
    void testHandleNoResourceFoundException() {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        ResponseEntity<Void> response = globalExceptionHandler.handleNoResourceFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testHandleHttpClientErrorException_Success() throws Exception {
        String json = "{\"status\":404, \"message\":\"Not Found\", \"path\":\"/test\"}";
        ErrorResponse errorResponse = new ErrorResponse(404, "Not Found", "/test");
        HttpStatusCodeException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found", json.getBytes(), null);

        when(objectMapper.readValue(any(String.class), eq(ErrorResponse.class))).thenReturn(errorResponse);

        String view = globalExceptionHandler.handleHttpClientErrorException(ex, request, model);

        assertEquals("error", view);
        verify(model).addAttribute("error", errorResponse);
    }

    @Test
    void testHandleHttpClientErrorException_ParsingFailure() throws Exception {
        HttpStatusCodeException ex = new HttpClientErrorException(HttpStatus.NOT_FOUND, "Not Found");
        when(objectMapper.readValue(any(String.class), eq(ErrorResponse.class))).thenThrow(new RuntimeException("Parsing error"));
        when(request.getRequestURI()).thenReturn("/test");

        String view = globalExceptionHandler.handleHttpClientErrorException(ex, request, model);

        assertEquals("error", view);
        verify(model).addAttribute(eq("error"), any(ErrorResponse.class));
    }

    @Test
    void testHandleHttpClientErrorException_WithReferer() throws Exception {
        String json = "{\"status\":409, \"message\":\"Conflict\", \"path\":\"/test\"}";
        ErrorResponse errorResponse = new ErrorResponse(409, "Conflict", "/test");
        HttpStatusCodeException ex = new HttpClientErrorException(HttpStatus.CONFLICT, "Conflict", json.getBytes(), null);

        when(objectMapper.readValue(any(String.class), eq(ErrorResponse.class))).thenReturn(errorResponse);
        when(request.getHeader("Referer")).thenReturn("http://localhost/previous");

        String view = globalExceptionHandler.handleHttpClientErrorException(ex, request, model);

        assertEquals("redirect:http://localhost/previous", view);
    }

    @Test
    void testHandleHttpClientErrorException_LoopPrevention() throws Exception {
        String json = "{\"status\":403, \"message\":\"Forbidden\", \"path\":\"/projects/1\"}";
        ErrorResponse errorResponse = new ErrorResponse(403, "Forbidden", "/projects/1");
        HttpStatusCodeException ex = new HttpClientErrorException(HttpStatus.FORBIDDEN, "Forbidden", json.getBytes(), null);

        when(objectMapper.readValue(any(String.class), eq(ErrorResponse.class))).thenReturn(errorResponse);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/projects/1");
        when(request.getHeader("Referer")).thenReturn("http://localhost:8080/projects/1");

        String view = globalExceptionHandler.handleHttpClientErrorException(ex, request, model);

        assertEquals("redirect:/my-projects", view);
    }

    @Test
    void testHandleValidationException_BindException() {
        BindException ex = mock(BindException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("obj", "field", "must not be blank")));
        when(request.getHeader("Referer")).thenReturn("http://localhost/form");

        String view = globalExceptionHandler.handleValidationException(ex, request, model);

        assertEquals("redirect:http://localhost/form", view);
    }

    @Test
    void testHandleValidationException_MethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(new FieldError("obj", "field", "invalid")));
        when(request.getHeader("Referer")).thenReturn(null);
        when(request.getRequestURI()).thenReturn("/submit");

        String view = globalExceptionHandler.handleValidationException(ex, request, model);

        assertEquals("error", view);
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
