package com.nhnacademy.gateway.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskUpdateRequest(
        @NotBlank @Size(max = 100) String title,
        @NotBlank String content
) {}
