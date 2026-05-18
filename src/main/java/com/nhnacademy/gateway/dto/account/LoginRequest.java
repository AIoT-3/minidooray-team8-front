package com.nhnacademy.gateway.dto.account;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String userId,
        @NotBlank String password
) {}
