package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record MilestoneCreateRequest(
        @NotBlank @Size(max = 50) String name,
        @NotNull LocalDate startDate,
        @NotNull LocalDate endDate
) {}
