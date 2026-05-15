package com.nhnacademy.gateway.dto.account;

public record LoginRequest(
        String userId,
        String password
) {}
