package com.sdm.login.auth;

import com.sdm.login.util.JwtUtil;
import com.sdm.login.util.WebUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

  @Setter
  private UserDetailsService userDetailsService;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
    super(authenticationManager);
  }

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint) {
    super(authenticationManager, authenticationEntryPoint);
  }

//  public void setUserDetailsService(UserDetailsService userDetailsService) {
//    this.userDetailsService = userDetailsService;
//  }



  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
    throws IOException, ServletException {
    try {
      // JWT -> Authentication
      String jwtHeader = request.getHeader("Authorization");
      if (jwtHeader != null && jwtHeader.startsWith("Bearer ")) {
        // 인증 자격 증명이 없는 경우에 발생
        String jwt = jwtHeader.substring(7);
        Claims claims = JwtUtil.getClaims(jwt);

        // Valid
        String ip = WebUtil.getIp(request);
        if (!claims.get("ip").equals(ip)) {
          // 제공된 자격 증명이 잘못되었을 때 발생
          throw new BadCredentialsException("JWT IP not matched");
        }
        String username = String.valueOf(claims.get("username"));
        String authorities = (String) claims.get("authorities");
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));

        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }
    catch (AuthenticationException ex) {
      SecurityContextHolder.clearContext();
      log.info("JWT credentials not valid: {}", ex.getMessage());
      return;
    }

    chain.doFilter(request, response);
  }

}
