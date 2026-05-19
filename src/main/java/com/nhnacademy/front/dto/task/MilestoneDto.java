package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MilestoneDto(
        @NotNull Long milestoneId,
        @NotBlank String name,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
