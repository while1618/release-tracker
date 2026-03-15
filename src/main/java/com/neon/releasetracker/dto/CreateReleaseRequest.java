package com.neon.releasetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Schema(description = "Request body for creating a new release")
public record CreateReleaseRequest(
    @Schema(description = "Release name", example = "Backend 2.1.0")
        @NotBlank(message = "{release.name.notBlank}")
        @Size(max = 255, message = "{release.name.size}")
        String name,
    @Schema(description = "Release description", example = "Core API improvements")
        @Size(max = 5000, message = "{release.description.size}")
        String description,
    @Schema(description = "Planned release date", example = "2025-06-30") LocalDate releaseDate) {}
