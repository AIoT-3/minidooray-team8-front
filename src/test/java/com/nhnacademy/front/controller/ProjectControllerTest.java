package com.nhnacademy.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.dto.task.*;
import com.nhnacademy.front.dto.ErrorResponse;
import com.nhnacademy.front.service.AccountApiService;
import com.nhnacademy.front.service.ProjectApiService;
import com.nhnacademy.front.service.TagApiService;
import com.nhnacademy.front.service.TaskApiService;
import com.nhnacademy.front.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({ProjectController.class, GlobalExceptionHandler.class})
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectApiService projectApiService;

    @MockitoBean
    private AccountApiService accountApiService;

    @MockitoBean
    private TagApiService tagApiService;

    @MockitoBean
    private TaskApiService taskApiService;

    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetProjectDetail_Success() throws Exception {
        ProjectDetailDto detail = new ProjectDetailDto(1L, "P1", "ACTIVE", "admin", List.of(), List.of(), List.of());
        when(projectApiService.getProjectDetail(1L)).thenReturn(detail);
        when(tagApiService.getTags(1L)).thenReturn(List.of());

        mockMvc.perform(get("/projects/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("project-detail"))
                .andExpect(model().attribute("project", detail));
    }

    @Test
    @WithMockUser
    void testGetProjectDetail_Forbidden() throws Exception {
        when(projectApiService.getProjectDetail(1L)).thenThrow(new HttpClientErrorException(HttpStatus.FORBIDDEN, "Not a member"));
        when(objectMapper.readValue(anyString(), eq(ErrorResponse.class)))
                .thenReturn(new ErrorResponse(403, "Not a member", "/projects/1"));

        mockMvc.perform(get("/projects/1")
                        .header("Referer", "/my-projects"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-projects"))
                .andExpect(flash().attribute("errorMessage", "Not a member"));
    }

    @Test
    @WithMockUser
    void testCreateProject_Success() throws Exception {
        mockMvc.perform(post("/projects")
                        .param("name", "New Project")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-projects"));
    }

    @Test
    @WithMockUser
    void testUpdateProject_Success() throws Exception {
        mockMvc.perform(post("/projects/1/edit")
                        .param("name", "Updated")
                        .param("status", "TERMINATED")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void testUpdateProject_Conflict() throws Exception {
        when(projectApiService.updateProject(any(), any())).thenThrow(new HttpClientErrorException(HttpStatus.CONFLICT, "PROJECT_NOT_ACTIVE"));
        when(objectMapper.readValue(anyString(), eq(ErrorResponse.class)))
                .thenReturn(new ErrorResponse(409,  "PROJECT_NOT_ACTIVE", "/projects/1/edit"));

        mockMvc.perform(post("/projects/1/edit")
                        .param("name", "Try Edit")
                        .param("status", "ACTIVE")
                        .header("Referer", "/projects/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"))
                .andExpect(flash().attribute("errorMessage", "PROJECT_NOT_ACTIVE"));
    }

    @Test
    @WithMockUser
    void testAddProjectMember_NotFound() throws Exception {
        when(accountApiService.getUser("none")).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND, "User not found"));
        when(objectMapper.readValue(anyString(), eq(ErrorResponse.class)))
                .thenReturn(new ErrorResponse(404, "User not found", "/projects/1/members"));

        mockMvc.perform(post("/projects/1/members")
                        .param("userId", "none")
                        .header("Referer", "/projects/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"))
                .andExpect(flash().attribute("errorMessage", "User not found"));
    }
}
