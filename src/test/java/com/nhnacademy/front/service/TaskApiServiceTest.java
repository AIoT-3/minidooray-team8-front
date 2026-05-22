package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TaskApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private TaskApiService taskApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        taskApiService = new TaskApiService(restTemplate);
    }

    @Test
    void testCreateTask() {
        Long projectId = 1L;
        TaskCreateRequest request = new TaskCreateRequest(null, projectId, "T1", "Content", "user1", null);
        TaskDto response = new TaskDto(1L, null, "T1", "Content", "user1", LocalDateTime.now(), List.of());
        when(restTemplate.postForEntity(eq("/projects/" + projectId + "/tasks"), eq(request), eq(TaskDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        TaskDto result = taskApiService.createTask(projectId, request);

        assertEquals("T1", result.title());
        verify(restTemplate).postForEntity("/projects/" + projectId + "/tasks", request, TaskDto.class);
    }

    @Test
    void testGetTask() {
        Long projectId = 1L;
        Long taskId = 1L;
        TaskDetailDto response = new TaskDetailDto(taskId, "T1", "Content", "user1", LocalDateTime.now(), null, List.of(), List.of());
        when(restTemplate.getForEntity("/projects/" + projectId + "/tasks/" + taskId, TaskDetailDto.class))
                .thenReturn(ResponseEntity.ok(response));

        TaskDetailDto result = taskApiService.getTask(projectId, taskId);

        assertEquals("T1", result.title());
        verify(restTemplate).getForEntity("/projects/" + projectId + "/tasks/" + taskId, TaskDetailDto.class);
    }

    @Test
    void testUpdateTask() {
        Long projectId = 1L;
        Long taskId = 1L;
        TaskUpdateRequest request = new TaskUpdateRequest("Updated", "Content");
        TaskDto response = new TaskDto(taskId, null, "Updated", "Content", "user1", LocalDateTime.now(), List.of());
        
        when(restTemplate.exchange(
                eq("/projects/" + projectId + "/tasks/" + taskId + "/edit"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(TaskDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        TaskDto result = taskApiService.updateTask(projectId, taskId, request);

        assertEquals("Updated", result.title());
    }

    @Test
    void testDeleteTask() {
        Long projectId = 1L;
        Long taskId = 1L;
        taskApiService.deleteTask(projectId, taskId);
        verify(restTemplate).delete("/projects/" + projectId + "/tasks/" + taskId);
    }

    @Test
    void testSetMilestone() {
        Long projectId = 1L;
        Long taskId = 1L;
        TaskMilestoneRequest request = new TaskMilestoneRequest(10L);
        
        taskApiService.setMilestone(projectId, taskId, request);
        
        verify(restTemplate).postForEntity("/projects/" + projectId + "/tasks/" + taskId + "/milestones", request, Void.class);
    }

    @Test
    void testAddTags() {
        Long projectId = 1L;
        Long taskId = 1L;
        TaskTagRequest request = new TaskTagRequest(List.of(1L, 2L));
        
        taskApiService.addTags(projectId, taskId, request);
        
        verify(restTemplate).postForEntity("/projects/" + projectId + "/tasks/" + taskId + "/tags", request, Void.class);
    }

    @Test
    void testGetTasksByTag() {
        Long projectId = 1L;
        Long tagId = 10L;
        TaskDto[] response = {new TaskDto(1L, null, "T1", "C1", "user1", LocalDateTime.now(), List.of())};
        when(restTemplate.getForEntity("/projects/" + projectId + "/tasks?tagId=" + tagId, TaskDto[].class))
                .thenReturn(ResponseEntity.ok(response));

        List<TaskDto> result = taskApiService.getTasksByTag(projectId, tagId);

        assertEquals(1, result.size());
        assertEquals("T1", result.getFirst().title());
    }

    @Test
    void testGetTasksByTag_NullBody() {
        Long projectId = 1L;
        Long tagId = 10L;
        when(restTemplate.getForEntity(anyString(), eq(TaskDto[].class)))
                .thenReturn(ResponseEntity.ok(null));

        List<TaskDto> result = taskApiService.getTasksByTag(projectId, tagId);

        assertTrue(result.isEmpty());
    }
}
