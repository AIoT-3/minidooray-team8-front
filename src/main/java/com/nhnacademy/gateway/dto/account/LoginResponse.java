package com.nhnacademy.gateway.dto.account;

import jakarta.validation.constraints.NotBlank;

public record LoginResponse(
        @NotBlank String userId
) {
}
