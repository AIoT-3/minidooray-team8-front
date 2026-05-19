package com.nhnacademy.front.dto.account;

import jakarta.validation.constraints.NotBlank;

public record UserStatusUpdateRequest(
        @NotBlank String status
) {}
