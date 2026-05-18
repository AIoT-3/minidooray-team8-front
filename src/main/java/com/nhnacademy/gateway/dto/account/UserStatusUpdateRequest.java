package com.nhnacademy.gateway.dto.account;

import jakarta.validation.constraints.NotBlank;

public record UserStatusUpdateRequest(
        @NotBlank String status
) {}
