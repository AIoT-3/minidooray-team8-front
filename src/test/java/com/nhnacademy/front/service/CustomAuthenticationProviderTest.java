package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.account.LoginRequest;
import com.nhnacademy.front.dto.account.LoginResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationProviderTest {

    @Mock
    private AccountApiService accountApiService;

    private CustomAuthenticationProvider provider;

    @BeforeEach
    void setUp() {
        provider = new CustomAuthenticationProvider(accountApiService);
    }

    @Test
    void testAuthenticate_Success() {
        LoginResponse loginResponse = new LoginResponse("user1");
        when(accountApiService.login(any(LoginRequest.class))).thenReturn(loginResponse);

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "pass");
        Authentication result = provider.authenticate(auth);

        assertNotNull(result);
        assertEquals("user1", result.getName());
        assertTrue(result.getPrincipal() instanceof UserDetails);
        assertEquals("user1", ((UserDetails) result.getPrincipal()).getUsername());
    }

    @Test
    void testAuthenticate_BadCredentials() {
        when(accountApiService.login(any())).thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Unauthorized"));

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "wrong");
        assertThrows(BadCredentialsException.class, () -> provider.authenticate(auth));
    }

    @Test
    void testAuthenticate_ApiError() {
        when(accountApiService.login(any())).thenThrow(new RuntimeException("API Down"));

        Authentication auth = new UsernamePasswordAuthenticationToken("user1", "pass");
        assertThrows(BadCredentialsException.class, () -> provider.authenticate(auth));
    }

    @Test
    void testSupports() {
        assertTrue(provider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(provider.supports(String.class));
    }
}
