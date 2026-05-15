package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.dto.task.TaskCreateRequest;
import com.nhnacademy.gateway.dto.task.TaskDetailDto;
import com.nhnacademy.gateway.dto.task.TaskDto;
import com.nhnacademy.gateway.dto.task.TaskMilestoneRequest;
import com.nhnacademy.gateway.dto.task.TaskTagRequest;
import com.nhnacademy.gateway.dto.task.TaskUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskApiService {

    private final RestTemplate restTemplate;
    private final String taskApiUrl;

    public TaskApiService(RestTemplate restTemplate, @Value("${minidooray.task-api.url}") String taskApiUrl) {
        this.restTemplate = restTemplate;
        this.taskApiUrl = taskApiUrl;
    }

    public TaskDto createTask(TaskCreateRequest request) {
        String url = taskApiUrl + "/tasks";
        ResponseEntity<TaskDto> response = restTemplate.postForEntity(url, request, TaskDto.class);
        return response.getBody();
    }

    public TaskDto updateTask(Long taskId, TaskUpdateRequest request) {
        String url = taskApiUrl + "/tasks/" + taskId;
        HttpEntity<TaskUpdateRequest> entity = new HttpEntity<>(request);
        ResponseEntity<TaskDto> response = restTemplate.exchange(url, HttpMethod.PUT, entity, TaskDto.class);
        return response.getBody();
    }

    public void deleteTask(Long taskId) {
        String url = taskApiUrl + "/tasks/" + taskId;
        restTemplate.delete(url);
    }

    public TaskDetailDto getTask(Long taskId) {
        String url = taskApiUrl + "/tasks/" + taskId;
        ResponseEntity<TaskDetailDto> response = restTemplate.getForEntity(url, TaskDetailDto.class);
        return response.getBody();
    }

    public void setMilestone(Long taskId, TaskMilestoneRequest request) {
        String url = taskApiUrl + "/tasks/" + taskId + "/milestones";
        restTemplate.postForEntity(url, request, Void.class);
    }

    public void addTags(Long taskId, TaskTagRequest request) {
        String url = taskApiUrl + "/tasks/" + taskId + "/tags";
        restTemplate.postForEntity(url, request, Void.class);
    }
}
