package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.dto.account.LoginRequest;
import com.nhnacademy.gateway.dto.account.LoginResponse;
import com.nhnacademy.gateway.dto.account.SignupRequest;
import com.nhnacademy.gateway.dto.account.SignupResponse;
import com.nhnacademy.gateway.dto.account.UserDto;
import com.nhnacademy.gateway.dto.account.UserStatusUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountApiService {
    private final RestTemplate restTemplate;
    private final String associationApiUrl;

    public AccountApiService(RestTemplate restTemplate,@Value("${minidooray.association-api.url}") String associationApiUrl) {
        this.restTemplate = restTemplate;
        this.associationApiUrl = associationApiUrl;
    }

    public SignupResponse signup(SignupRequest request) {
        String url = associationApiUrl + "/accounts/signup";
        ResponseEntity<SignupResponse> response = restTemplate.postForEntity(
                url,
                request,
                SignupResponse.class
        );
        return response.getBody();
    }

    public LoginResponse login(LoginRequest request) {
        String url = associationApiUrl + "/accounts/login";
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                url,
                request,
                LoginResponse.class
        );
        return response.getBody();
    }

    public void updateUserStatus(String userId, UserStatusUpdateRequest request) {
        String url = associationApiUrl + "/accounts/users/" + userId + "/status";
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class
        );
    }

    public UserDto getUser(String userId) {
        String url = associationApiUrl + "/accounts/users/" + userId;
        ResponseEntity<UserDto> response = restTemplate.getForEntity(
                url,
                UserDto.class
        );
        return response.getBody();
    }
}
