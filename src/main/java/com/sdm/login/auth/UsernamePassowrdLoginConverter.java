package com.sdm.login.auth;

import com.sdm.login.util.WebUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UsernamePassowrdLoginConverter implements AuthenticationConverter {
  @Override
  public Authentication convert(HttpServletRequest request) {
    Map<String, Object> jsonMap = WebUtil.extractRequestToMap(request);
    String username = (String) jsonMap.get("username");
    String password = (String) jsonMap.get("password");
    username = (username != null) ? username.trim() : "";
    password = (password != null) ? password : "";

    UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username, password);
    return authRequest;
  }
}
