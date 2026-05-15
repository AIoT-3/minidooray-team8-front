package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.dto.task.TagCreateRequest;
import com.nhnacademy.gateway.dto.task.TagDto;
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
public class TagApiService {
    private final RestTemplate restTemplate;
    private final String taskApiUrl;

    public TagApiService(RestTemplate restTemplate, @Value("${minidooray.task-api.url}") String taskApiUrl) {
        this.restTemplate = restTemplate;
        this.taskApiUrl = taskApiUrl;
    }

    public TagDto createTag(Long projectId, TagCreateRequest request) {
        String url = taskApiUrl + "/projects/" + projectId + "/tags";
        ResponseEntity<TagDto> response = restTemplate.postForEntity(url, request, TagDto.class);
        return response.getBody();
    }

    public TagDto updateTag(Long projectId, Long tagId, TagCreateRequest request) {
        String url = taskApiUrl + "/projects/" + projectId + "/tags/" + tagId;
        HttpEntity<TagCreateRequest> entity = new HttpEntity<>(request);
        ResponseEntity<TagDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, TagDto.class);
        return response.getBody();
    }

    public void deleteTag(Long projectId, Long tagId) {
        String url = taskApiUrl + "/projects/" + projectId + "/tags/" + tagId;
        restTemplate.delete(url);
    }

    public List<TagDto> getTags(Long projectId) {
        String url = taskApiUrl + "/projects/" + projectId + "/tags";
        ResponseEntity<TagDto[]> response = restTemplate.getForEntity(url, TagDto[].class);
        TagDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }
}
