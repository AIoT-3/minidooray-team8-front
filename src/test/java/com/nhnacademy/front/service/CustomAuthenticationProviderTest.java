package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.account.LoginRequest;
import com.nhnacademy.front.dto.account.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomAuthenticationProviderTest {

    @Mock
    private AccountApiService accountApiService;

    private CustomAuthenticationProvider customAuthenticationProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customAuthenticationProvider = new CustomAuthenticationProvider(accountApiService);
    }

    @Test
    void testAuthenticate_Success() {
        Authentication authRequest = new UsernamePasswordAuthenticationToken("user1", "pass");
        LoginResponse loginResponse = new LoginResponse("user1");

        when(accountApiService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        Authentication result = customAuthenticationProvider.authenticate(authRequest);

        assertNotNull(result);
        assertEquals("user1", result.getName());
        assertTrue(result.isAuthenticated());
    }

    @Test
    void testAuthenticate_BadCredentials() {
        Authentication authRequest = new UsernamePasswordAuthenticationToken("user1", "wrong");
        
        when(accountApiService.login(any(LoginRequest.class))).thenThrow(new BadCredentialsException("Invalid"));

        assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(authRequest));
    }

    @Test
    void testSupports() {
        assertTrue(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
