package com.nhnacademy.front.dto.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
        @NotBlank String userId,
        @NotBlank @Email String email,
        @NotBlank String password,
        @NotBlank String status
) {}
