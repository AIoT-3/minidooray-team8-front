package com.nhnacademy.gateway.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.gateway.service.TagApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TagApiService tagApiService;

    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testCreateTag() throws Exception {
        mockMvc.perform(post("/projects/1/tags")
                        .param("name", "Tag1")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }

    @Test
    @WithMockUser
    void testDeleteTag() throws Exception {
        mockMvc.perform(post("/projects/1/tags/2/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects/1"));
    }
}
