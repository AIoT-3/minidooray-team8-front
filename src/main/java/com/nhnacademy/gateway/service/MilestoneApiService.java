package com.nhnacademy.gateway.service;


import com.nhnacademy.gateway.dto.task.MilestoneCreateRequest;
import com.nhnacademy.gateway.dto.task.MilestoneDetailDto;
import com.nhnacademy.gateway.dto.task.MilestoneDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MilestoneApiService {
    private final RestTemplate restTemplate;
    private final String taskApiUrl;

    public MilestoneApiService(RestTemplate restTemplate, @Value("${minidooray.task-api.url}") String taskApiUrl) {
        this.restTemplate = restTemplate;
        this.taskApiUrl = taskApiUrl;
    }

    public MilestoneDto createMilestone(Long projectId, MilestoneCreateRequest request) {
        String url = taskApiUrl + "/projects/" + projectId + "/milestones";
        ResponseEntity<MilestoneDto> response = restTemplate.postForEntity(url, request, MilestoneDto.class);
        return response.getBody();
    }

    public MilestoneDto updateMilestone(Long projectId, Long milestoneId, MilestoneCreateRequest request) {
        String url = taskApiUrl + "/projects/" + projectId + "/milestones/" + milestoneId;
        HttpEntity<MilestoneCreateRequest> entity = new HttpEntity<>(request);
        ResponseEntity<MilestoneDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, MilestoneDto.class);
        return response.getBody();
    }

    public void deleteMilestone(Long projectId, Long milestoneId) {
        String url = taskApiUrl + "/projects/" + projectId + "/milestones/" + milestoneId;
        restTemplate.delete(url);
    }

    public MilestoneDetailDto getMilestone(Long projectId, Long milestoneId) {
        String url = taskApiUrl + "/projects/" + projectId + "/milestones/" + milestoneId;

        ResponseEntity<MilestoneDetailDto> response = restTemplate.getForEntity(url, MilestoneDetailDto.class);
        return response.getBody();
    }

    public List<MilestoneDto> getMilestones(Long projectId) {
        String url = taskApiUrl + "/projects/" + projectId + "/milestones";
        ResponseEntity<MilestoneDto[]> response = restTemplate.getForEntity(url, MilestoneDto[].class);
        MilestoneDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }
}

