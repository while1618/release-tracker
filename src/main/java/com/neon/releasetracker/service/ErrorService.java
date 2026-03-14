package com.neon.releasetracker.service;

public interface ErrorService {
  String getMessage(String code);

  String getMessage(String code, Object... args);
}
