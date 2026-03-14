package com.neon.releasetracker.dto;

import com.neon.releasetracker.model.ReleaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateReleaseRequest(
    @NotBlank(message = "{release.name.notBlank}")
    @Size(max = 255, message = "{release.name.size}")
    String name,

    @Size(max = 5000, message = "{release.description.size}")
    String description,

    @NotNull(message = "{release.status.notNull}")
    ReleaseStatus status,

    LocalDate releaseDate) {}
