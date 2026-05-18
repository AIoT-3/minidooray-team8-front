package com.nhnacademy.gateway.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record TaskDto(
        @NotNull Long taskId,
        Long milestoneId,
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank String writerId,
        @NotNull LocalDateTime createdAt,
        List<TagDto> tags
) {}
