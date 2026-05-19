package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.task.TagCreateRequest;
import com.nhnacademy.front.dto.task.TagDto;
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

    public TagApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TagDto createTag(Long projectId, TagCreateRequest request) {
        String url = "/projects/" + projectId + "/tags";
        ResponseEntity<TagDto> response = restTemplate.postForEntity(url, request, TagDto.class);
        return response.getBody();
    }

    public TagDto updateTag(Long projectId, Long tagId, TagCreateRequest request) {
        String url = "/projects/" + projectId + "/tags/" + tagId;
        HttpEntity<TagCreateRequest> entity = new HttpEntity<>(request);
        ResponseEntity<TagDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, TagDto.class);
        return response.getBody();
    }

    public void deleteTag(Long projectId, Long tagId) {
        String url = "/projects/" + projectId + "/tags/" + tagId;
        restTemplate.delete(url);
    }

    public List<TagDto> getTags(Long projectId) {
        String url = "/projects/" + projectId + "/tags";
        ResponseEntity<TagDto[]> response = restTemplate.getForEntity(url, TagDto[].class);
        TagDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }
}
