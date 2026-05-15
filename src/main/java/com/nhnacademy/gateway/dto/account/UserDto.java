package com.nhnacademy.gateway.dto.account;

public record UserDto(
        String userId,
        String email,
        String password,
        String status
) {}
