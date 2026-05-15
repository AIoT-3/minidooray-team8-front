package com.nhnacademy.gateway.dto.task;

public record ProjectUpdateRequest(
        String name,
        String status // ACTIVE, DORMANT, CLOSED
) {}
