package com.nhnacademy.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.service.MilestoneApiService;
import com.nhnacademy.front.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest({MilestoneController.class, GlobalExceptionHandler.class})
class MilestoneControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MilestoneApiService milestoneApiService;

    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testCreateMilestone_Success() throws Exception {
        mockMvc.perform(post("/projects/1/milestones")
                        .param("name", "M1")
                        .param("startDate", "2024-01-01")
                        .param("endDate", "2024-12-31")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void testCreateMilestone_ValidationFailure() throws Exception {
        // Missing dates
        mockMvc.perform(post("/projects/1/milestones")
                        .param("name", "M1")
                        .header("Referer", "/projects/1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"))
                .andExpect(flash().attributeExists("errorMessage"));
    }

    @Test
    @WithMockUser
    void testDeleteMilestone() throws Exception {
        mockMvc.perform(post("/projects/1/milestones/2/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }
}
