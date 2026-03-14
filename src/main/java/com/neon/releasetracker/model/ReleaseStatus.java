package com.neon.releasetracker.model;

import com.fasterxml.jackson.annotation.JsonValue;

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
}
