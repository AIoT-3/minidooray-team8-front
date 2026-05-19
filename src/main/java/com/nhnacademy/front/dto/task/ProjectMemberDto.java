package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;

public record ProjectMemberDto(
        @NotBlank String userId
) {}
