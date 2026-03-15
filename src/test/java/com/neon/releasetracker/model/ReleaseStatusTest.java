package com.neon.releasetracker.model;

import static com.neon.releasetracker.model.ReleaseStatus.CREATED;
import static com.neon.releasetracker.model.ReleaseStatus.DONE;
import static com.neon.releasetracker.model.ReleaseStatus.IN_DEVELOPMENT;
import static com.neon.releasetracker.model.ReleaseStatus.ON_DEV;
import static com.neon.releasetracker.model.ReleaseStatus.ON_PROD;
import static com.neon.releasetracker.model.ReleaseStatus.ON_STAGING;
import static com.neon.releasetracker.model.ReleaseStatus.QA_DONE_ON_DEV;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ReleaseStatusTest {

  @Test
  void canTransitionTo_oneStepForward_returnsTrue() {
    assertTrue(CREATED.canTransitionTo(IN_DEVELOPMENT));
    assertTrue(IN_DEVELOPMENT.canTransitionTo(ON_DEV));
    assertTrue(ON_DEV.canTransitionTo(QA_DONE_ON_DEV));
    assertTrue(ON_PROD.canTransitionTo(DONE));
  }

  @Test
  void canTransitionTo_oneStepBackward_returnsTrue() {
    assertTrue(IN_DEVELOPMENT.canTransitionTo(CREATED));
    assertTrue(ON_DEV.canTransitionTo(IN_DEVELOPMENT));
    assertTrue(ON_STAGING.canTransitionTo(QA_DONE_ON_DEV));
  }

  @Test
  void canTransitionTo_sameStatus_returnsFalse() {
    assertFalse(CREATED.canTransitionTo(CREATED));
    assertFalse(IN_DEVELOPMENT.canTransitionTo(IN_DEVELOPMENT));
    assertFalse(DONE.canTransitionTo(DONE));
  }

  @Test
  void canTransitionTo_skipForward_returnsFalse() {
    assertFalse(CREATED.canTransitionTo(ON_DEV));
    assertFalse(CREATED.canTransitionTo(DONE));
    assertFalse(IN_DEVELOPMENT.canTransitionTo(QA_DONE_ON_DEV));
  }

  @Test
  void canTransitionTo_skipBackward_returnsFalse() {
    assertFalse(ON_DEV.canTransitionTo(CREATED));
    assertFalse(DONE.canTransitionTo(IN_DEVELOPMENT));
    assertFalse(ON_STAGING.canTransitionTo(IN_DEVELOPMENT));
  }

  @Test
  void canTransitionTo_fromDone_alwaysReturnsFalse() {
    for (ReleaseStatus status : ReleaseStatus.values()) {
      assertFalse(DONE.canTransitionTo(status));
    }
  }
}
