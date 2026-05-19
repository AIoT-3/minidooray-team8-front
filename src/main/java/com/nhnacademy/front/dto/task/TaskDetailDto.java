package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

public record TaskDetailDto(
        @NotNull Long taskId,
        @NotBlank String title,
        @NotBlank String content,
        @NotBlank String writerId,
        @NotNull LocalDateTime createdAt,
        MilestoneDto milestone,
        List<TagDto> tags,
        List<CommentDto> comments
) {
}
