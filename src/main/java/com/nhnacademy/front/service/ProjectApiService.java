package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.task.ProjectCreateRequest;
import com.nhnacademy.front.dto.task.ProjectDetailDto;
import com.nhnacademy.front.dto.task.ProjectDto;
import com.nhnacademy.front.dto.task.ProjectUpdateRequest;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProjectApiService {

    private final RestTemplate restTemplate;

    public ProjectApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ProjectDto createProject(ProjectCreateRequest requestDto) {
        String url = "/projects";
        ResponseEntity<ProjectDto> response = restTemplate.postForEntity(
                url,
                requestDto,
                ProjectDto.class
        );

        return response.getBody();
    }

    public ProjectDto updateProject(Long projectId, ProjectUpdateRequest requestDto) {
        String url = "/projects/" + projectId;
        
        HttpEntity<ProjectUpdateRequest> entity = new HttpEntity<>(requestDto);
        ResponseEntity<ProjectDto> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                ProjectDto.class
        );
        return response.getBody();
    }

    public ProjectDto getProject(Long projectId) {
        String url = "/projects/" + projectId;
        ResponseEntity<ProjectDto> response = restTemplate.getForEntity(
                url,
                ProjectDto.class
        );
        return response.getBody();
    }

    public ProjectDetailDto getProjectDetail(Long projectId) {
        String url = "/projects/" + projectId;
        ResponseEntity<ProjectDetailDto> response = restTemplate.getForEntity(
                url,
                ProjectDetailDto.class
        );
        return response.getBody();
    }

    public List<ProjectDto> getProjects() {
        String url = "/projects";
        ResponseEntity<ProjectDto[]> response = restTemplate.getForEntity(
                url,
                ProjectDto[].class
        );
        ProjectDto[] body = response.getBody();
        return body != null ? List.of(body) : Collections.emptyList();
    }

    public void addProjectMember(Long projectId, com.nhnacademy.front.dto.task.ProjectMemberRequest requestDto) {
        String url = "/projects/" + projectId + "/members";
        restTemplate.postForEntity(url, requestDto, Void.class);
    }
}
