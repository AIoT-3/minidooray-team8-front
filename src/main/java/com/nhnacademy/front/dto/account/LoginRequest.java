package com.nhnacademy.front.dto.account;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String userId,
        @NotBlank String password
) {}
