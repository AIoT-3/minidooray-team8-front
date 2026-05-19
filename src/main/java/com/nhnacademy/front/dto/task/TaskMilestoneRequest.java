package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotNull;

public record TaskMilestoneRequest(
        @NotNull Long milestoneId
) {
}
