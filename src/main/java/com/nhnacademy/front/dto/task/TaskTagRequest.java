package com.nhnacademy.front.dto.task;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record TaskTagRequest(
        @NotEmpty List<Long> tagIds
) {}
