package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;

public record ProjectUpdateRequest(
        @NotBlank String name,
        @NotBlank String status // ACTIVE, DORMANT, TERMINATED
) {}
