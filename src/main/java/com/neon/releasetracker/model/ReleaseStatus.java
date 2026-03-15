package com.neon.releasetracker.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Set;

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

  private Set<ReleaseStatus> allowedNext;

  static {
    CREATED.allowedNext = Set.of(IN_DEVELOPMENT);
    IN_DEVELOPMENT.allowedNext = Set.of(CREATED, ON_DEV);
    ON_DEV.allowedNext = Set.of(IN_DEVELOPMENT, QA_DONE_ON_DEV);
    QA_DONE_ON_DEV.allowedNext = Set.of(ON_DEV, ON_STAGING);
    ON_STAGING.allowedNext = Set.of(QA_DONE_ON_DEV, QA_DONE_ON_STAGING);
    QA_DONE_ON_STAGING.allowedNext = Set.of(ON_STAGING, ON_PROD);
    ON_PROD.allowedNext = Set.of(QA_DONE_ON_STAGING, DONE);
    DONE.allowedNext = Set.of();
  }

  public boolean canTransitionTo(ReleaseStatus target) {
    return allowedNext.contains(target);
  }
}
