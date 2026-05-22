package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.account.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AccountApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private AccountApiService accountApiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountApiService = new AccountApiService(restTemplate);
    }

    @Test
    void testSignup() {
        SignupRequest request = new SignupRequest("user1234", "user@test.com", "password123");
        SignupResponse response = new SignupResponse("user1234", "ACTIVE");
        when(restTemplate.postForEntity("/accounts/signup", request, SignupResponse.class))
                .thenReturn(ResponseEntity.ok(response));

        SignupResponse result = accountApiService.signup(request);

        assertEquals("user1234", result.id());
        verify(restTemplate).postForEntity("/accounts/signup", request, SignupResponse.class);
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("user1", "pass");
        LoginResponse response = new LoginResponse("user1");
        when(restTemplate.postForEntity("/accounts/login", request, LoginResponse.class))
                .thenReturn(ResponseEntity.ok(response));

        LoginResponse result = accountApiService.login(request);

        assertEquals("user1", result.userId());
        verify(restTemplate).postForEntity("/accounts/login", request, LoginResponse.class);
    }

    @Test
    void testUpdateUserStatus() {
        UserStatusUpdateRequest request = new UserStatusUpdateRequest("DORMANT");
        
        accountApiService.updateUserStatus("user1", request);
        
        verify(restTemplate).exchange(
                eq("/accounts/users/user1/status"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)
        );
    }

    @Test
    void testGetUser() {
        UserDto response = new UserDto("user1", "user@test.com", "pass", "ACTIVE");
        when(restTemplate.getForEntity("/accounts/users/user1", UserDto.class))
                .thenReturn(ResponseEntity.ok(response));

        UserDto result = accountApiService.getUser("user1");

        assertEquals("user1", result.userId());
        verify(restTemplate).getForEntity("/accounts/users/user1", UserDto.class);
    }
}
