package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

public record TaskCreateRequest(
        Long taskId,
        Long projectId,
        @NotBlank @Size(max = 100) String title,
        @NotBlank String content,
        String writerId,
        LocalDateTime createdAt
) {}
