package com.neon.releasetracker.error;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorMessage {
  private final LocalDateTime timestamp;
  private final int status;
  private final String error;
  private final List<String> messages;

  public ErrorMessage(HttpStatus status) {
    this.timestamp = LocalDateTime.now();
    this.status = status.value();
    this.error = status.getReasonPhrase();
    this.messages = new ArrayList<>();
  }

  public void addMessage(String message) {
    this.messages.add(message);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("{\"timestamp\":\"");
    sb.append(timestamp.format(DateTimeFormatter.ISO_DATE_TIME));
    sb.append("\",\"status\":");
    sb.append(status);
    sb.append(",\"error\":\"");
    sb.append(error);
    sb.append("\",\"messages\":[");
    for (int i = 0; i < messages.size(); i++) {
      if (i > 0) sb.append(",");
      sb.append("\"").append(messages.get(i)).append("\"");
    }
    sb.append("]}");
    return sb.toString();
  }
}
