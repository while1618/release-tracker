package com.neon.releasetracker.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public record CreateReleaseRequest(
    @NotBlank String name,
    String description,
    LocalDate releaseDate) {}
