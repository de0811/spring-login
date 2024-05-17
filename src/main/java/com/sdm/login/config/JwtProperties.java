package com.sdm.login.config;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtProperties {
    private String secretKey = "sando"; // jwt 비밀키
    private long expirationMinuteTime = 30; // session 만료 시간
    private String tokenPrefix = "Bearer ";
    private String headerString = "Authorization"; // 헤더에 전달되는 토큰의 이름
}
