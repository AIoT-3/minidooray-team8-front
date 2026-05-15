package com.nhnacademy.gateway.dto.task;

import java.time.LocalDate;

public record MilestoneDto(
        Long milestoneId,
        String name,
        LocalDate startDate,
        LocalDate endDate
) {}
