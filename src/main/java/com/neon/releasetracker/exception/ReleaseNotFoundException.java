package com.neon.releasetracker.exception;

public class ReleaseNotFoundException extends RuntimeException {

  public ReleaseNotFoundException(Long id) {
    super("Release not found with id: " + id);
  }
}
