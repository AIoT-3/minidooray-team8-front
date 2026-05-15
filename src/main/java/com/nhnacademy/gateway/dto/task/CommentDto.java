package com.nhnacademy.gateway.dto.task;

import java.time.LocalDateTime;

public record CommentDto(
        Long commentId,
        String writerId,
        String content,
        LocalDateTime createdAt
) {
}
