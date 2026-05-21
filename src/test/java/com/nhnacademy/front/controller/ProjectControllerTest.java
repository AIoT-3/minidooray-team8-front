package com.nhnacademy.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.dto.task.*;
import com.nhnacademy.front.service.AccountApiService;
import com.nhnacademy.front.service.ProjectApiService;
import com.nhnacademy.front.service.TagApiService;
import com.nhnacademy.front.service.TaskApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
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
    void testGetProjectDetail() throws Exception {
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
    void testCreateProject() throws Exception {
        mockMvc.perform(post("/projects")
                        .param("name", "New Project")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-projects"));
    }

    @Test
    @WithMockUser
    void testUpdateProject() throws Exception {
        mockMvc.perform(post("/projects/1/edit")
                        .param("name", "Updated")
                        .param("status", "TERMINATED")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void testCloseProject() throws Exception {
        mockMvc.perform(post("/projects/1/close")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-projects"));
    }

    @Test
    @WithMockUser
    void testAddProjectMember() throws Exception {
        mockMvc.perform(post("/projects/1/members")
                        .param("userId", "user1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }
}
