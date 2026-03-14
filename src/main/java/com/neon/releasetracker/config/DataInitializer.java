package com.neon.releasetracker.config;

import com.neon.releasetracker.model.Release;
import com.neon.releasetracker.model.ReleaseStatus;
import com.neon.releasetracker.repository.ReleaseRepository;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"dev", "demo"})
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

  private static final int RELEASE_COUNT = 30;

  private final ReleaseRepository releaseRepository;

  @Override
  public void run(ApplicationArguments args) {
    if (releaseRepository.count() > 0) {
      return;
    }

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
