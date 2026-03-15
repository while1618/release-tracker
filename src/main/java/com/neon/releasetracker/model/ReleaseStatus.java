package com.neon.releasetracker.model;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Release lifecycle status",
    allowableValues = {
      "Created",
      "In Development",
      "On DEV",
      "QA Done on DEV",
      "On Staging",
      "QA Done on Staging",
      "On PROD",
      "Done"
    })
public enum ReleaseStatus {
  CREATED("Created"),
  IN_DEVELOPMENT("In Development"),
  ON_DEV("On DEV"),
  QA_DONE_ON_DEV("QA Done on DEV"),
  ON_STAGING("On Staging"),
  QA_DONE_ON_STAGING("QA Done on Staging"),
  ON_PROD("On PROD"),
  DONE("Done");

  private final String displayName;

  ReleaseStatus(String displayName) {
    this.displayName = displayName;
  }

  @JsonValue
  public String getDisplayName() {
    return displayName;
  }

  public boolean canTransitionTo(ReleaseStatus next) {
    if (this == DONE) {
      return false;
    }
    return Math.abs(next.ordinal() - this.ordinal()) == 1;
  }
}
