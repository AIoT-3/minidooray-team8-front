package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ProjectDetailDto(
        @NotNull Long projectId,
        @NotBlank String name,
        @NotBlank String status,
        @NotBlank String adminId,
        List<ProjectMemberDto> members,
        List<TaskDto> tasks,
        List<MilestoneDto> milestones
) {}

