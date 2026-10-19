package com.project.tracker.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProjectRequestRecord(
        @NotBlank @Size(max = 100) String name,
        @Size(max = 250) String description
        ) {}
