package com.nhnacademy.gateway.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskMilestoneRequest(
        @NotNull Long milestoneId
) {
}
