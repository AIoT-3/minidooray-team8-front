package com.nhnacademy.gateway.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ErrorResponse (
        @NotNull int status,
        @NotBlank String message,
        @NotBlank String path
){}
