package com.sdm.login.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BasicControllerAdvice {

  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(Exception.class)
  public String exception(Exception e) {
    log.info("exception: {}", e.getMessage(), e);

    return e.getMessage();
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  public String illegalArgumentException(IllegalArgumentException e) {
    log.info("IllegalArgumentException: {}", e.getMessage(), e);

    return e.getMessage();
  }

}
