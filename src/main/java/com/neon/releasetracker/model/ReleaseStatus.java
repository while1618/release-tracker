package com.neon.releasetracker.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
    description = "Release lifecycle status",
    allowableValues = {
      "CREATED",
      "IN_DEVELOPMENT",
      "ON_DEV",
      "QA_DONE_ON_DEV",
      "ON_STAGING",
      "QA_DONE_ON_STAGING",
      "ON_PROD",
      "DONE"
    })
public enum ReleaseStatus {
  CREATED,
  IN_DEVELOPMENT,
  ON_DEV,
  QA_DONE_ON_DEV,
  ON_STAGING,
  QA_DONE_ON_STAGING,
  ON_PROD,
  DONE;

  public boolean canTransitionTo(ReleaseStatus next) {
    if (this == DONE) {
      return false;
    }
    return Math.abs(next.ordinal() - this.ordinal()) == 1;
  }
}
