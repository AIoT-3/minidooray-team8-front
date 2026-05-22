package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.task.TaskCreateRequest;
import com.nhnacademy.front.dto.task.TaskDetailDto;
import com.nhnacademy.front.dto.task.TaskDto;
import com.nhnacademy.front.dto.task.TaskMilestoneRequest;
import com.nhnacademy.front.dto.task.TaskTagRequest;
import com.nhnacademy.front.dto.task.TaskUpdateRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TaskApiService {

    private final RestTemplate restTemplate;

    public TaskApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public TaskDto createTask(Long projectId, TaskCreateRequest request) {
        String url = "/projects/" + projectId + "/tasks";
        ResponseEntity<TaskDto> response = restTemplate.postForEntity(url, request, TaskDto.class);
        return response.getBody();
    }

    public TaskDto updateTask(Long projectId, Long taskId, TaskUpdateRequest request) {
        String url = "/projects/" + projectId + "/tasks/" + taskId + "/edit";
        HttpEntity<TaskUpdateRequest> entity = new HttpEntity<>(request);
        ResponseEntity<TaskDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, TaskDto.class);
        return response.getBody();
    }

    public void deleteTask(Long projectId, Long taskId) {
        String url = "/projects/" + projectId + "/tasks/" + taskId;
        restTemplate.delete(url);
    }

    public TaskDetailDto getTask(Long projectId, Long taskId) {
        String url = "/projects/" + projectId + "/tasks/" + taskId;
        ResponseEntity<TaskDetailDto> response = restTemplate.getForEntity(url, TaskDetailDto.class);
        return response.getBody();
    }

    public void setMilestone(Long projectId, Long taskId, TaskMilestoneRequest request) {
        String url = "/projects/" + projectId + "/tasks/" + taskId + "/milestones";
        restTemplate.postForEntity(url, request, Void.class);
    }

    public void addTags(Long projectId, Long taskId, TaskTagRequest request) {
        String url = "/projects/" + projectId + "/tasks/" + taskId + "/tags";
        restTemplate.postForEntity(url, request, Void.class);
    }

    public List<TaskDto> getTasksByTag(Long projectId, Long tagId) {
        String url = "/projects/" + projectId + "/tasks?tagId=" + tagId;
        ResponseEntity<TaskDto[]> response = restTemplate.getForEntity(url, TaskDto[].class);
        TaskDto[] body = response.getBody();
        return body != null ? Arrays.asList(body) : Collections.emptyList();
    }
}
