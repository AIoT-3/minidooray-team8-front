package com.nhnacademy.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.dto.task.*;
import com.nhnacademy.front.service.MilestoneApiService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({TaskController.class, GlobalExceptionHandler.class})
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaskApiService taskApiService;
    @MockitoBean
    private MilestoneApiService milestoneApiService;
    @MockitoBean
    private TagApiService tagApiService;
    @MockitoBean
    private ProjectApiService projectApiService;

    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testCreateTask_Success() throws Exception {
        TaskDto task = new TaskDto(1L, null, "T1", "C1", "user1", LocalDateTime.now(), List.of());
        when(taskApiService.createTask(eq(1L), any(TaskCreateRequest.class))).thenReturn(task);

        mockMvc.perform(post("/projects/1/tasks")
                        .param("title", "T1")
                        .param("content", "C1")
                        .param("writerId", "user1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void testCreateTask_ValidationFailure() throws Exception {
        // Missing content
        mockMvc.perform(post("/projects/1/tasks")
                        .param("title", "T1")
                        .header("Referer", "/projects/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    @WithMockUser
    void testGetTask_Success() throws Exception {
        TaskDetailDto task = new TaskDetailDto(1L, "T1", "C1", "user1", LocalDateTime.now(), null, List.of(), List.of());
        ProjectDetailDto project = new ProjectDetailDto(1L, "P1", "ACTIVE", "admin", List.of(), List.of(), List.of());
        when(taskApiService.getTask(1L, 1L)).thenReturn(task);
        when(projectApiService.getProjectDetail(1L)).thenReturn(project);

        mockMvc.perform(get("/projects/1/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("task-detail"))
                .andExpect(model().attribute("task", task));
    }

    @Test
    @WithMockUser
    void testDeleteTask() throws Exception {
        mockMvc.perform(post("/projects/1/tasks/1/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }
}
