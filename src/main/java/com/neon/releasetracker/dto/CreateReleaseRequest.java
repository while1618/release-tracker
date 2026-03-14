package com.neon.releasetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CreateReleaseRequest(
    @NotBlank(message = "{release.name.notBlank}") @Size(max = 255, message = "{release.name.size}")
        String name,
    @Size(max = 5000, message = "{release.description.size}") String description,
    LocalDate releaseDate) {}
