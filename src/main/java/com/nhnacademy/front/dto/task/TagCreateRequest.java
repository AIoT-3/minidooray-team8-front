package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TagCreateRequest(
        @NotBlank @Size(max = 20) String name
) {}
