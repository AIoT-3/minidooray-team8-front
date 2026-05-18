package com.nhnacademy.gateway.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TagDto(
        @NotNull Long tagId,
        @NotBlank String name
) {}
