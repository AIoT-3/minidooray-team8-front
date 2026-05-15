package com.nhnacademy.gateway.dto.account;

public record SignupRequest(
        String id,
        String email,
        String password
) {}
