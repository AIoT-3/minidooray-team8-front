package com.nhnacademy.front.service;


import com.nhnacademy.front.dto.task.MilestoneCreateRequest;
import com.nhnacademy.front.dto.task.MilestoneDetailDto;
import com.nhnacademy.front.dto.task.MilestoneDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MilestoneApiService {
    private final RestTemplate restTemplate;

    public MilestoneApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public MilestoneDto createMilestone(Long projectId, MilestoneCreateRequest request) {
        String url = "/projects/" + projectId + "/milestones";
        ResponseEntity<MilestoneDto> response = restTemplate.postForEntity(url, request, MilestoneDto.class);
        return response.getBody();
    }

    public MilestoneDto updateMilestone(Long projectId, Long milestoneId, MilestoneCreateRequest request) {
        String url = "/projects/" + projectId + "/milestones/" + milestoneId;
        HttpEntity<MilestoneCreateRequest> entity = new HttpEntity<>(request);
        ResponseEntity<MilestoneDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, MilestoneDto.class);
        return response.getBody();
    }

    public void deleteMilestone(Long projectId, Long milestoneId) {
        String url = "/projects/" + projectId + "/milestones/" + milestoneId;
        restTemplate.delete(url);
    }

    public MilestoneDetailDto getMilestone(Long projectId, Long milestoneId) {
        String url = "/projects/" + projectId + "/milestones/" + milestoneId;

        ResponseEntity<MilestoneDetailDto> response = restTemplate.getForEntity(url, MilestoneDetailDto.class);
        return response.getBody();
    }

    public List<MilestoneDto> getMilestones(Long projectId) {
        String url = "/projects/" + projectId + "/milestones";
        ResponseEntity<MilestoneDto[]> response = restTemplate.getForEntity(url, MilestoneDto[].class);
        MilestoneDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }
}

