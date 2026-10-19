package com.project.tracker.project;

import java.time.Instant;

public record ProjectResponseRecord(
        Long id,
        String name,
        String description,
        Instant createdAt
) {}
