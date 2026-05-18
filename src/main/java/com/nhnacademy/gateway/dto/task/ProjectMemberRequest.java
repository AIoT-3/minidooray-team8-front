package com.nhnacademy.gateway.dto.task;

import jakarta.validation.constraints.NotBlank;

public record ProjectMemberRequest(
        @NotBlank String userId
) {}
