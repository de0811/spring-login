package com.sdm.login.auth;

import com.sdm.login.config.JwtProperties;
import com.sdm.login.util.JwtUtil;
import com.sdm.login.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
  final private JwtProperties jwtProperties;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    //JWT 토큰 생성
    writeTokenResponse(request, response, authentication);
  }
  private void writeTokenResponse(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
    log.info("JWT 토큰 생성");
    Map<String, String> claims = new HashMap<String, String>();

    String ip = WebUtil.getIp(request);
    claims.put("ip", ip); // JWT 탈취 방지
    claims.put("username", authentication.getName());
    String strAuthorities = authentication.getAuthorities().toString();
    claims.put("authorities", String.join( ",", AuthorityUtils.authorityListToSet(authentication.getAuthorities()) ));
    String jwt = JwtUtil.generate("issuer", "subject", jwtProperties.getExpirationMinuteTime(), claims);

    // Test
//    Claims claims2 = JwtUtil.getClaims(jwt);
//    log.info("claims: {}", claims2);
    ///

    response.setContentType("text/html;charset=UTF-8");

    response.addHeader("Authorization", "Bearer " + jwt);
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_OK);

    var writer = response.getWriter();
    writer.println("{\"token\":\"" + jwt + "\"}");
    writer.flush();
  }
}
