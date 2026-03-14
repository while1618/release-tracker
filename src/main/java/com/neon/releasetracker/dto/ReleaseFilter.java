package com.neon.releasetracker.dto;

import com.neon.releasetracker.model.ReleaseStatus;
import java.time.LocalDate;

public record ReleaseFilter(
    String name,
    ReleaseStatus status,
    LocalDate releaseDateFrom,
    LocalDate releaseDateTo) {}
