package com.neon.releasetracker;

import org.springframework.boot.SpringApplication;

public class TestReleasetrackerApplication {

  public static void main(String[] args) {
    SpringApplication.from(ReleasetrackerApplication::main)
        .with(TestcontainersConfiguration.class)
        .run(args);
  }
}
