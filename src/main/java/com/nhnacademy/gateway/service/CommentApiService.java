package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.dto.task.CommentCreateRequest;
import com.nhnacademy.gateway.dto.task.CommentDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CommentApiService {
    private final RestTemplate restTemplate;
    private final String taskApiUrl;

    public CommentApiService(RestTemplate restTemplate, @Value("${minidooray.task-api.url}") String taskApiUrl) {
        this.restTemplate = restTemplate;
        this.taskApiUrl = taskApiUrl;
    }

    public CommentDto createComment(Long projectId, Long taskId, CommentCreateRequest request) {
        String url = taskApiUrl + "/projects/" + projectId + "/tasks/" + taskId + "/comments";
        ResponseEntity<CommentDto> response = restTemplate.postForEntity(url, request, CommentDto.class);
        return response.getBody();
    }

    public CommentDto updateComment(Long projectId, Long taskId, Long commentId, CommentCreateRequest request) {
        String url = taskApiUrl + "/projects/" + projectId + "/tasks/" + taskId + "/comments/" + commentId;
        HttpEntity<CommentCreateRequest> entity = new HttpEntity<>(request);
        ResponseEntity<CommentDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, CommentDto.class);
        return response.getBody();
    }

    public void deleteComment(Long projectId, Long taskId, Long commentId) {
        String url = taskApiUrl + "/projects/" + projectId + "/tasks/" + taskId + "/comments/" + commentId;
        restTemplate.delete(url);
    }

    public List<CommentDto> getComments(Long projectId, Long taskId) {
        String url = taskApiUrl + "/projects/" + projectId + "/tasks/" + taskId + "/comments";
        ResponseEntity<CommentDto[]> response = restTemplate.getForEntity(url, CommentDto[].class);
        CommentDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }
}
