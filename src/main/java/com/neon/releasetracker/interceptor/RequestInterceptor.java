package com.neon.releasetracker.interceptor;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class RequestInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull Object handler) {
    final var requestId = UUID.randomUUID().toString();
    response.setHeader("X-Request-ID", requestId);
    MDC.put("REQUEST_ID", requestId);
    MDC.put("REQUEST_METHOD", request.getMethod());
    MDC.put("REQUEST_URL", request.getRequestURL().toString());
    return true;
  }

  @Override
  public void afterCompletion(
      @Nonnull HttpServletRequest request,
      @Nonnull HttpServletResponse response,
      @Nonnull Object handler,
      Exception ex) {
    MDC.clear();
  }
}
