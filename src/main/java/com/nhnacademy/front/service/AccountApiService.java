package com.nhnacademy.front.service;

import com.nhnacademy.front.dto.account.LoginRequest;
import com.nhnacademy.front.dto.account.LoginResponse;
import com.nhnacademy.front.dto.account.SignupRequest;
import com.nhnacademy.front.dto.account.SignupResponse;
import com.nhnacademy.front.dto.account.UserDto;
import com.nhnacademy.front.dto.account.UserStatusUpdateRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountApiService {
    private final RestTemplate restTemplate;

    public AccountApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public SignupResponse signup(SignupRequest request) {
        String url = "/accounts/signup";
        ResponseEntity<SignupResponse> response = restTemplate.postForEntity(
                url,
                request,
                SignupResponse.class
        );
        return response.getBody();
    }

    public LoginResponse login(LoginRequest request) {
        String url =  "/accounts/login";
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                url,
                request,
                LoginResponse.class
        );
        return response.getBody();
    }

    public void updateUserStatus(String userId, UserStatusUpdateRequest request) {
        String url = "/accounts/users/" + userId + "/status";
        restTemplate.exchange(
                url,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                Void.class
        );
    }

    public UserDto getUser(String userId) {
        String url = "/accounts/users/" + userId;
        ResponseEntity<UserDto> response = restTemplate.getForEntity(
                url,
                UserDto.class
        );
        return response.getBody();
    }
}
