package com.sdm.login.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

/*
Servlet, Spring Security, jjwt
dependency
	implementation 'io.jsonwebtoken:jjwt-api:0.12.5'
	runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.5'
	runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.5'
 */
public class JwtUtil {
  private static SecretKey secretKey = null;
  public static SecretKey getSecretKey() {
    // 암호 자동 관리로 아에 빼버리고 시간마다 새로 갱신되도록 만드는게 더 나을거 같은데
    if( secretKey == null ) {
      secretKey = Jwts.SIG.HS256.key().build();
    }
    return secretKey;
  }
  public static String generate(String issuer, String subject, Long expirationMinuteTime, Map<String, String> claim) {
//    SecretKey secretKey = Keys.hmacShaKeyFor( strSecretKey.getBytes(StandardCharsets.UTF_8) );
    SecretKey secretKey = getSecretKey();

    String jwt = Jwts.builder()
      .issuer(issuer)
      .subject(subject)
      .claims(claim)
      .signWith(secretKey)
      .issuedAt(new Date())
      .expiration(new Date(System.currentTimeMillis() + (1000L * 60 * expirationMinuteTime)))
      .compact();

    return jwt;
  }

  public static Claims getClaims(String jwt) throws BadCredentialsException {
    try {
//      SecretKey secretKey = Keys.hmacShaKeyFor(strSecretKey.getBytes(StandardCharsets.UTF_8));
      SecretKey secretKey = getSecretKey();

      Claims claims = Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(jwt)
        .getPayload();
      return claims;

    } catch (Exception e) {
      throw new BadCredentialsException("Invalid Token received!");
    }
  }

  public static Authentication getAuthentication(String jwt) {
    Claims claims = getClaims(jwt);
    String username = String.valueOf(claims.get("username"));
    String authorities = (String) claims.get("authorities");
    Authentication auth = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
    SecurityContextHolder.getContext().setAuthentication(auth);

    return auth;
  }
}
