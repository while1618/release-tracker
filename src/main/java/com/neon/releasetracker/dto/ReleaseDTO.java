package com.neon.releasetracker.dto;

import com.neon.releasetracker.model.ReleaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.time.LocalDate;

@Schema(description = "Release details")
public record ReleaseDTO(
    @Schema(description = "Unique identifier", example = "1") Long id,
    @Schema(description = "Release name", example = "Backend 2.1.0") String name,
    @Schema(description = "Release description", example = "Core API improvements")
        String description,
    @Schema(description = "Current release status", example = "Created") ReleaseStatus status,
    @Schema(description = "Planned release date", example = "2025-06-30") LocalDate releaseDate,
    @Schema(description = "Timestamp when the release was created") Instant createdAt,
    @Schema(description = "Timestamp when the release was last updated") Instant lastUpdatedAt) {}
