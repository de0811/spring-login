package com.sdm.login.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/error")
public class ErrorController {
  @GetMapping("/400")
  public String error400() {
    throw new IllegalArgumentException("잘못된 요청입니다.");
  }

  @GetMapping("/500")
  public String error500() throws Exception {
    throw new Exception("서버 에러입니다.");
  }

  @GetMapping("")
  public String error() {
    throw new IllegalArgumentException("에러에러하다아");
  }

}
