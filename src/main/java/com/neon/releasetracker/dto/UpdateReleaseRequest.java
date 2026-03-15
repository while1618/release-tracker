package com.neon.releasetracker.dto;

import com.neon.releasetracker.model.ReleaseStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Request body for updating an existing release")
public record UpdateReleaseRequest(
    @Schema(description = "Release name", example = "Backend 2.1.0")
        @NotBlank(message = "{release.name.notBlank}")
        @Size(max = 255, message = "{release.name.size}")
        String name,
    @Schema(description = "Release description", example = "Core API improvements")
        @Size(max = 5000, message = "{release.description.size}")
        String description,
    @Schema(
            description = "Release status. Must transition one step at a time; DONE is terminal",
            example = "In Development")
        @NotNull(message = "{release.status.notNull}")
        ReleaseStatus status,
    @Schema(description = "Planned release date", example = "2025-06-30") LocalDate releaseDate) {}
