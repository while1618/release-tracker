package com.neon.releasetracker.config;

import com.neon.releasetracker.model.Release;
import com.neon.releasetracker.model.ReleaseStatus;
import com.neon.releasetracker.repository.ReleaseRepository;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "demo", "test"})
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private static final int RELEASE_COUNT = 30;

  private final ReleaseRepository releaseRepository;
  private final Environment environment;

  @Override
  public void run(ApplicationArguments args) {
    if (releaseRepository.count() > 0) {
      return;
    }

    if (environment.acceptsProfiles(Profiles.of("test"))) {
      seedTestData();
    } else {
      seedFakerData();
    }
  }

  private void seedTestData() {
    releaseRepository.saveAll(
        List.of(
            Release.builder()
                .name("Backend 2.1.0")
                .description("Core API improvements")
                .status(ReleaseStatus.CREATED)
                .releaseDate(LocalDate.of(2025, 1, 10))
                .build(),
            Release.builder()
                .name("Frontend 3.0.0")
                .description("UI redesign")
                .status(ReleaseStatus.IN_DEVELOPMENT)
                .releaseDate(LocalDate.of(2025, 2, 20))
                .build(),
            Release.builder()
                .name("Mobile 1.5.0")
                .description("Mobile performance updates")
                .status(ReleaseStatus.ON_DEV)
                .releaseDate(LocalDate.of(2025, 3, 5))
                .build(),
            Release.builder()
                .name("Infra 4.0.0")
                .description("Infrastructure overhaul")
                .status(ReleaseStatus.QA_DONE_ON_DEV)
                .releaseDate(LocalDate.of(2025, 4, 15))
                .build(),
            Release.builder()
                .name("Security 1.2.0")
                .description("Security patches")
                .status(ReleaseStatus.ON_STAGING)
                .releaseDate(LocalDate.of(2025, 5, 1))
                .build(),
            Release.builder()
                .name("Auth 2.0.0")
                .description("Authentication revamp")
                .status(ReleaseStatus.DONE)
                .releaseDate(LocalDate.of(2025, 6, 30))
                .build()));
  }

  private void seedFakerData() {
    final var faker = new Faker();
    final var statuses = ReleaseStatus.values();
    final var releases = new ArrayList<Release>(RELEASE_COUNT);

    for (int i = 0; i < RELEASE_COUNT; i++) {
      final var name = faker.app().version();
      final var description = faker.app().name();
      final var releaseDate =
          faker.timeAndDate().past().atZone(ZoneId.systemDefault()).toLocalDate();
      final var status = statuses[faker.number().numberBetween(0, statuses.length)];

      releases.add(
          Release.builder()
              .name(name)
              .description(description)
              .releaseDate(releaseDate)
              .status(status)
              .build());
    }

    releaseRepository.saveAll(List.copyOf(releases));
  }
}
