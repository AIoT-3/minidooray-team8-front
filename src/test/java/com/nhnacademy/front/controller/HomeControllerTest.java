package com.nhnacademy.front.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.front.service.ProjectApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectApiService projectApiService;

    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testIndex() throws Exception {
        when(projectApiService.getProjects()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/my-projects"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("projects"))
                .andExpect(model().attributeExists("currentStatus"));
    }
}
