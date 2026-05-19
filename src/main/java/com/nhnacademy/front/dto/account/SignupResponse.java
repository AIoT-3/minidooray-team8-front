package com.nhnacademy.front.dto.account;

import jakarta.validation.constraints.NotBlank;

public record SignupResponse(
        @NotBlank String id,
        @NotBlank String status
) {
}
