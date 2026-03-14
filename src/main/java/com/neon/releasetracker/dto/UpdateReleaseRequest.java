package com.neon.releasetracker.dto;

import com.neon.releasetracker.model.ReleaseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record UpdateReleaseRequest(
    @NotBlank String name,
    String description,
    @NotNull ReleaseStatus status,
    LocalDate releaseDate) {}
