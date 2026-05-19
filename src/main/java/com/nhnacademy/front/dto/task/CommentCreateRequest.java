package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotBlank @Size(max = 200) String content
) {}
