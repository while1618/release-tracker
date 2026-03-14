package com.neon.releasetracker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
class FlywayMigrationIT extends TestcontainersConfiguration {

  @Test
  void migrationRunsAndSchemaIsValid() {}
}
