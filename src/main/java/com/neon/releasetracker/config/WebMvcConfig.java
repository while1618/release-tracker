package com.neon.releasetracker.config;

import com.neon.releasetracker.interceptor.RequestInterceptor;
import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  private final RequestInterceptor requestInterceptor;

  public WebMvcConfig(RequestInterceptor requestInterceptor) {
    this.requestInterceptor = requestInterceptor;
  }

  @Override
  public void addInterceptors(@Nonnull InterceptorRegistry registry) {
    registry.addInterceptor(requestInterceptor);
  }
}
