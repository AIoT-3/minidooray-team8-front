package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;

public record ProjectMemberRequest(
        @NotBlank String userId
) {}
