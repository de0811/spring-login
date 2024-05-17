package com.sdm.login.auth;

import com.sdm.login.util.JwtUtil;
import com.sdm.login.util.WebUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.stereotype.Component;

/**
 원래 Converter 기능을 해야하는 것이지만 BasicAuthenticationFilter 역활의 주체를 담당
 아니 이러면 response 에 누가 넣어줘?
 새로 갱신해주려고 했는데 이럼 갱신 못해주는데?
 */

@RequiredArgsConstructor
@Component
public class JwtAuthenticationConverter implements AuthenticationConverter {
  private final PrincipalDetailsService principalDetailsService;

  @Override
  public Authentication convert(HttpServletRequest request) {
    String jwtHeader = request.getHeader("Authorization");
    if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
      // 인증 자격 증명이 없는 경우에 발생
      throw new AuthenticationCredentialsNotFoundException("JWT not found in Authorization header");
    }
    String jwt = jwtHeader.substring(7);
    Claims claims = JwtUtil.getClaims(jwt);
    String ip = WebUtil.getIp(request);
    if( !claims.get("ip").equals(ip) ) {
      // 제공된 자격 증명이 잘못되었을 때 발생
      throw new BadCredentialsException("JWT IP not matched");
    }
    String username = String.valueOf(claims.get("username"));
    String authorities = (String) claims.get("authorities");
    PrincipalDetails principalDetails = (PrincipalDetails) principalDetailsService.loadUserByUsername(username);
    Authentication auth = new UsernamePasswordAuthenticationToken(principalDetails, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

    SecurityContextHolder.getContext().setAuthentication(auth);

    return auth;
  }
}
