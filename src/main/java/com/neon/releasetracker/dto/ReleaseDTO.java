package com.neon.releasetracker.dto;

import com.neon.releasetracker.model.ReleaseStatus;
import java.time.Instant;
import java.time.LocalDate;

public record ReleaseDTO(
    Long id,
    String name,
    String description,
    ReleaseStatus status,
    LocalDate releaseDate,
    Instant createdAt,
    Instant lastUpdatedAt) {}
