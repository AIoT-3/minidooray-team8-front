package com.nhnacademy.gateway.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProjectDto(
        @NotNull Long projectId,
        @NotBlank String name,
        @NotBlank String status
) {}

