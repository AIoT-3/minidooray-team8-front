package com.nhnacademy.gateway.dto.task;

public record TaskUpdateRequest(
        String title,
        String content
) {}
