package com.neon.releasetracker.logger;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Component
public class CustomLogger {
  public void info(String message) {
    final var logDetails = new LogDetails();
    log.info(
        "REQUEST_ID: {}, ENDPOINT: {} {}, MESSAGE: {}",
        logDetails.getRequestId(),
        logDetails.getRequestMethod(),
        logDetails.getRequestUrl(),
        message);
  }

  public void error(String message, Exception e) {
    final var logDetails = new LogDetails();
    log.error(
        "REQUEST_ID: {}, ENDPOINT: {} {}, MESSAGE: {}",
        logDetails.getRequestId(),
        logDetails.getRequestMethod(),
        logDetails.getRequestUrl(),
        message,
        e);
  }

  @Getter
  private static class LogDetails {
    private final String requestId;
    private final String requestMethod;
    private final String requestUrl;

    public LogDetails() {
      final var request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      requestId = MDC.get("REQUEST_ID");
      requestMethod = request.getMethod();
      requestUrl = request.getRequestURL().toString();
    }
  }
}
