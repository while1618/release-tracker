package com.neon.releasetracker.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ReleaseNotFoundException extends RuntimeException {

  private final HttpStatus status;
  private final Long id;

  public ReleaseNotFoundException(String message, Long id) {
    super(message);
    this.id = id;
    this.status = HttpStatus.NOT_FOUND;
  }
}
