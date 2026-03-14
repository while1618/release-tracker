package com.neon.releasetracker.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class InvalidStatusTransitionException extends RuntimeException {

  private final HttpStatus status;
  private final Object[] args;

  public InvalidStatusTransitionException(Object... args) {
    super("release.invalidStatusTransition");
    this.status = HttpStatus.BAD_REQUEST;
    this.args = args;
  }
}
