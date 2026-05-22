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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
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
        ProjectDto response = new ProjectDto(1L, "P1", "ACTIVE", "admin");
        when(restTemplate.postForEntity(eq("/projects"), eq(request), eq(ProjectDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDto result = projectApiService.createProject(request);

        assertEquals("P1", result.name());
        verify(restTemplate).postForEntity("/projects", request, ProjectDto.class);
    }

    @Test
    void testUpdateProject() {
        ProjectUpdateRequest request = new ProjectUpdateRequest("P2", "ACTIVE");
        ProjectDto response = new ProjectDto(1L, "P2", "DORMANT", "user1");
        when(restTemplate.exchange(eq("/projects/1/edit"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ProjectDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDto result = projectApiService.updateProject(1L, request);

        assertEquals("P2", result.name());
        verify(restTemplate).exchange(eq("/projects/1/edit"), eq(HttpMethod.PUT), any(HttpEntity.class), eq(ProjectDto.class));
    }

    @Test
    void testCloseProject() {
        ProjectDto response = new ProjectDto(1L, "P1", "TERMINATED", "user1");
        when(restTemplate.exchange(eq("/projects/1/close"), eq(HttpMethod.PUT), eq(null), eq(ProjectDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDto result = projectApiService.closeProject(1L);

        assertEquals("TERMINATED", result.status());
        verify(restTemplate).exchange(eq("/projects/1/close"), eq(HttpMethod.PUT), eq(null), eq(ProjectDto.class));
    }

    @Test
    void testGetProject() {
        ProjectDto response = new ProjectDto(1L, "P1", "ACTIVE", "admin");
        when(restTemplate.getForEntity(eq("/projects/1"), eq(ProjectDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDto result = projectApiService.getProject(1L);

        assertEquals(1L, result.projectId());
        verify(restTemplate).getForEntity("/projects/1", ProjectDto.class);
    }

    @Test
    void testGetProjectDetail() {
        ProjectDetailDto response = new ProjectDetailDto(1L, "P1", "ACTIVE", "admin", List.of(), List.of(), List.of());
        when(restTemplate.getForEntity("/projects/1", ProjectDetailDto.class))
                .thenReturn(ResponseEntity.ok(response));

        ProjectDetailDto result = projectApiService.getProjectDetail(1L);

        assertEquals("P1", result.name());
        verify(restTemplate).getForEntity("/projects/1", ProjectDetailDto.class);
    }

    @Test
    void testGetProjects() {
        ProjectDto[] response = {new ProjectDto(1L, "P1", "ACTIVE", "admin")};
        when(restTemplate.getForEntity(eq("/projects"), eq(ProjectDto[].class)))
                .thenReturn(ResponseEntity.ok(response));

        List<ProjectDto> result = projectApiService.getProjects();

        assertEquals(1, result.size());
        assertEquals("P1", result.get(0).name());
        verify(restTemplate).getForEntity("/projects", ProjectDto[].class);
    }

    @Test
    void testGetProjects_NullBody() {
        when(restTemplate.getForEntity(anyString(), eq(ProjectDto[].class)))
                .thenReturn(ResponseEntity.ok(null));

        List<ProjectDto> result = projectApiService.getProjects();

        assertTrue(result.isEmpty());
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
