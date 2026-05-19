package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectCreateRequest(
        @NotBlank @Size(max = 100) String name
) {}
