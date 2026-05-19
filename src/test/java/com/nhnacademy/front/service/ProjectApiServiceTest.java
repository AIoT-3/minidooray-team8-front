package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.task.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private ProjectApiService projectApiService;

    @BeforeEach
    void setUp() {
        projectApiService = new ProjectApiService(restTemplate);
    }

    @Test
    void testCreateProject() {
        ProjectCreateRequest request = new ProjectCreateRequest("P1");
        ProjectDto response = new ProjectDto(1L, "P1", "ACTIVE");
        when(restTemplate.postForEntity(eq("/projects"), eq(request), eq(ProjectDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDto result = projectApiService.createProject(request);

        assertEquals("P1", result.name());
        verify(restTemplate).postForEntity("/projects", request, ProjectDto.class);
    }

    @Test
    void testUpdateProject() {
        ProjectUpdateRequest request = new ProjectUpdateRequest("P2", "CLOSED");
        ProjectDto response = new ProjectDto(1L, "P2", "CLOSED");
        when(restTemplate.exchange(eq("/projects/1"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ProjectDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDto result = projectApiService.updateProject(1L, request);

        assertEquals("P2", result.name());
        verify(restTemplate).exchange(eq("/projects/1"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ProjectDto.class));
    }

    @Test
    void testGetProject() {
        ProjectDto response = new ProjectDto(1L, "P1", "ACTIVE");
        when(restTemplate.getForEntity(eq("/projects/1"), eq(ProjectDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDto result = projectApiService.getProject(1L);

        assertEquals(1L, result.projectId());
        verify(restTemplate).getForEntity("/projects/1", ProjectDto.class);
    }

    @Test
    void testGetProjectDetail() {
        ProjectDetailDto response = new ProjectDetailDto(1L, "P1", "ACTIVE", "admin", List.of(), List.of(), List.of());
        when(restTemplate.getForEntity(eq("/projects/1"), eq(ProjectDetailDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDetailDto result = projectApiService.getProjectDetail(1L);

        assertEquals("P1", result.name());
        verify(restTemplate).getForEntity("/projects/1", ProjectDetailDto.class);
    }

    @Test
    void testGetProjects() {
        ProjectDto[] response = {new ProjectDto(1L, "P1", "ACTIVE")};
        when(restTemplate.getForEntity(eq("/projects"), eq(ProjectDto[].class)))
                .thenReturn(ResponseEntity.ok(response));

        List<ProjectDto> result = projectApiService.getProjects();

        assertEquals(1, result.size());
        assertEquals("P1", result.get(0).name());
        verify(restTemplate).getForEntity("/projects", ProjectDto[].class);
    }

    @Test
    void testAddProjectMember() {
        ProjectMemberRequest request = new ProjectMemberRequest("user1");
        when(restTemplate.postForEntity(eq("/projects/1/members"), eq(request), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build());

        projectApiService.addProjectMember(1L, request);

        verify(restTemplate).postForEntity("/projects/1/members", request, Void.class);
    }
}
