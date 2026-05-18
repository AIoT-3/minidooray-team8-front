package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.dto.account.*;
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
    private final String associationApiUrl = "http://test-api";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        accountApiService = new AccountApiService(restTemplate, associationApiUrl);
    }

    @Test
    void testSignup() {
        SignupRequest request = new SignupRequest("user1", "user@test.com", "pass");
        SignupResponse response = new SignupResponse("user1", "ACTIVE");
        when(restTemplate.postForEntity(eq(associationApiUrl + "/accounts/signup"), eq(request), eq(SignupResponse.class)))
                .thenReturn(ResponseEntity.ok(response));

        SignupResponse result = accountApiService.signup(request);

        assertEquals("user1", result.id());
        verify(restTemplate).postForEntity(associationApiUrl + "/accounts/signup", request, SignupResponse.class);
    }

    @Test
    void testLogin() {
        LoginRequest request = new LoginRequest("user1", "pass");
        LoginResponse response = new LoginResponse("user1");
        when(restTemplate.postForEntity(eq(associationApiUrl + "/accounts/login"), eq(request), eq(LoginResponse.class)))
                .thenReturn(ResponseEntity.ok(response));

        LoginResponse result = accountApiService.login(request);

        assertEquals("user1", result.userId());
        verify(restTemplate).postForEntity(associationApiUrl + "/accounts/login", request, LoginResponse.class);
    }

    @Test
    void testUpdateUserStatus() {
        UserStatusUpdateRequest request = new UserStatusUpdateRequest("DORMANT");
        
        accountApiService.updateUserStatus("user1", request);
        
        verify(restTemplate).exchange(
                eq(associationApiUrl + "/accounts/users/user1/status"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(Void.class)
        );
    }

    @Test
    void testGetUser() {
        UserDto response = new UserDto("user1", "user@test.com", "pass", "ACTIVE");
        when(restTemplate.getForEntity(eq(associationApiUrl + "/accounts/users/user1"), eq(UserDto.class)))
                .thenReturn(ResponseEntity.ok(response));

        UserDto result = accountApiService.getUser("user1");

        assertEquals("user1", result.userId());
        verify(restTemplate).getForEntity(associationApiUrl + "/accounts/users/user1", UserDto.class);
    }
}
