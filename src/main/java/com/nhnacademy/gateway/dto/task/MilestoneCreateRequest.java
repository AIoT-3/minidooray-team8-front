package com.nhnacademy.gateway.dto.task;

import java.time.LocalDate;

public record MilestoneCreateRequest(
        String name,
        LocalDate startDate,
        LocalDate endDate
) {}
