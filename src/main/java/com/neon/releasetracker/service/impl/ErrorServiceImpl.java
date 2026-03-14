package com.neon.releasetracker.service.impl;

import com.neon.releasetracker.service.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ErrorServiceImpl implements ErrorService {

  private final MessageSource messageSource;

  @Override
  public String getMessage(String code) {
    return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
  }

  @Override
  public String getMessage(String code, Object... args) {
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }
}
