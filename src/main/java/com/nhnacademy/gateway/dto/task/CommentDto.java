package com.nhnacademy.gateway.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CommentDto(
        @NotNull Long commentId,
        @NotBlank String writerId,
        @NotBlank String content,
        @NotNull LocalDateTime createdAt
) {
}
