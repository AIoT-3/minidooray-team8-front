package com.nhnacademy.front.controller;

import com.nhnacademy.front.dto.account.SignupRequest;
import com.nhnacademy.front.dto.account.SignupResponse;
import com.nhnacademy.front.service.AccountApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountApiService accountApiService;

    @MockitoBean
    private ObjectMapper objectMapper;

    @Test
    void testLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testSignupForm() throws Exception {
        mockMvc.perform(get("/signup"))
                .andExpect(status().isOk())
                .andExpect(view().name("signup"));
    }

    @Test
    @WithMockUser
    void testSignup() throws Exception {
        SignupResponse response = new SignupResponse("user1", "ACTIVE");
        when(accountApiService.signup(any(SignupRequest.class))).thenReturn(response);

        mockMvc.perform(post("/signup")
                        .param("id", "user1")
                        .param("email", "test@test.com")
                        .param("password", "password123") // Should be at least 8 chars
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @WithMockUser
    void testUpdateUserStatus() throws Exception {
        mockMvc.perform(post("/users/user1/status")
                        .param("status", "INACTIVE")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/my-projects"));
    }
}
