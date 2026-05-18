package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.dto.task.CommentCreateRequest;
import com.nhnacademy.gateway.dto.task.CommentDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CommentApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private CommentApiService commentApiService;
    private final String taskApiUrl = "http://test-task-api";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        commentApiService = new CommentApiService(restTemplate, taskApiUrl);
    }

    @Test
    void testCreateComment() {
        CommentCreateRequest request = new CommentCreateRequest("Content");
        CommentDto response = new CommentDto(1L, "user1", "Content", LocalDateTime.now());
        when(restTemplate.postForEntity(anyString(), eq(request), eq(CommentDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        CommentDto result = commentApiService.createComment(1L, 2L, request);

        assertEquals("Content", result.content());
    }

    @Test
    void testUpdateComment() {
        CommentCreateRequest request = new CommentCreateRequest("Updated Content");
        CommentDto response = new CommentDto(3L, "user1", "Updated Content", LocalDateTime.now());
        
        when(restTemplate.exchange(
                anyString(),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(CommentDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        CommentDto result = commentApiService.updateComment(1L, 2L, 3L, request);

        assertEquals("Updated Content", result.content());
    }

    @Test
    void testDeleteComment() {
        commentApiService.deleteComment(1L, 2L, 3L);
        verify(restTemplate).delete(anyString());
    }
}
