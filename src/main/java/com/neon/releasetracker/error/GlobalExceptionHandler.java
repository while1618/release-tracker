package com.neon.releasetracker.error;

import com.neon.releasetracker.error.exception.InvalidStatusTransitionException;
import com.neon.releasetracker.error.exception.ReleaseNotFoundException;
import com.neon.releasetracker.logger.CustomLogger;
import com.neon.releasetracker.service.ErrorService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RequiredArgsConstructor
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
  private final ErrorService errorService;
  private final CustomLogger customLogger;

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      @Nonnull MethodArgumentNotValidException e,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatusCode statusCode,
      @Nonnull WebRequest request) {
    customLogger.error("Validation failed", e);
    final var status = (HttpStatus) statusCode;
    final var errorMessage = new ErrorMessage(status);
    final var result = e.getBindingResult();
    result.getFieldErrors().forEach(error -> errorMessage.addMessage(error.getDefaultMessage()));
    result.getGlobalErrors().forEach(error -> errorMessage.addMessage(error.getDefaultMessage()));
    return new ResponseEntity<>(errorMessage, setHeaders(), status);
  }

  private ResponseEntity<Object> createError(HttpStatus status, String message) {
    final var errorMessage = new ErrorMessage(status);
    errorMessage.addMessage(message);
    return new ResponseEntity<>(errorMessage, setHeaders(), status);
  }

  private HttpHeaders setHeaders() {
    final var header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_PROBLEM_JSON);
    return header;
  }

  @ExceptionHandler({InvalidStatusTransitionException.class})
  public ResponseEntity<Object> handleInvalidStatusTransition(InvalidStatusTransitionException e) {
    return createError(e.getStatus(), errorService.getMessage(e.getMessage(), e.getArgs()));
  }

  @ExceptionHandler({ReleaseNotFoundException.class})
  public ResponseEntity<Object> handleResourceNotFoundException(ReleaseNotFoundException e) {
    return createError(e.getStatus(), errorService.getMessage(e.getMessage(), e.getId()));
  }

  @ExceptionHandler({Exception.class})
  public ResponseEntity<Object> handleGlobalException(Exception e) {
    customLogger.error("Unhandled exception", e);
    return createError(
        HttpStatus.INTERNAL_SERVER_ERROR, errorService.getMessage("server.internalError"));
  }

  @Override
  protected ResponseEntity<Object> handleMissingServletRequestParameter(
      @Nonnull MissingServletRequestParameterException e,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatusCode statusCode,
      @Nonnull WebRequest request) {
    customLogger.error("Missing request parameter", e);
    return createError(
        (HttpStatus) statusCode, errorService.getMessage("request.parameterMissing"));
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      @Nonnull HttpRequestMethodNotSupportedException e,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatusCode statusCode,
      @Nonnull WebRequest request) {
    customLogger.error("HTTP method not supported", e);
    return createError(
        (HttpStatus) statusCode, errorService.getMessage("request.methodNotSupported"));
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      @Nonnull HttpMessageNotReadableException e,
      @Nonnull HttpHeaders headers,
      @Nonnull HttpStatusCode statusCode,
      @Nonnull WebRequest request) {
    customLogger.error("HTTP message not readable", e);
    return createError(
        (HttpStatus) statusCode, errorService.getMessage("request.messageNotReadable"));
  }

  @ExceptionHandler({MethodArgumentTypeMismatchException.class})
  public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException e) {
    customLogger.error("Method argument type mismatch", e);
    return createError(
        HttpStatus.BAD_REQUEST, errorService.getMessage("request.parameterTypeMismatch"));
  }
}
