package com.sdm.login.config;

import com.sdm.login.auth.JwtAuthenticationFilter;
import com.sdm.login.auth.UsernamePassowrdLoginConverter;
import com.sdm.login.auth.JwtAuthenticationSuccessHandler;
import com.sdm.login.oauth.PrincipalOauth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity(debug = true)
@Configuration // IoC 빈(bean)을 등록
public class SecurityConfig {

	final private PrincipalOauth2UserService principalOauth2UserService;

	// login form / JSON login 처리
	final private UsernamePassowrdLoginConverter usernamePassowrdLoginConverter;
	final private JwtAuthenticationSuccessHandler jwtAuthenticationSuccessHandler;

	// JwtAuthorizationFilter 에서 사용
	final private UserDetailsService userDetailsService;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
		http.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());
		http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.disable());
		http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.authorizeHttpRequests(authorizeRequests ->
				authorizeRequests.requestMatchers("/user/**").authenticated()
					.requestMatchers("/admin/**").hasRole("ADMIN")
					.anyRequest().permitAll()
		);
		http.formLogin(AbstractHttpConfigurer::disable);

		// form login / JSON login 처리
		AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager, usernamePassowrdLoginConverter);
		authenticationFilter.setRequestMatcher(new AntPathRequestMatcher("/api/login", "POST"));
		authenticationFilter.setSuccessHandler(jwtAuthenticationSuccessHandler);
		http.addFilterAfter(authenticationFilter, LogoutFilter.class);

		http.oauth2Login(oauth2LoginCustomizer ->
			oauth2LoginCustomizer.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint.userService(principalOauth2UserService))
				.successHandler(jwtAuthenticationSuccessHandler)
				.loginPage("/error")
		);

		// JWT 검증
		http.httpBasic(AbstractHttpConfigurer::disable); // BasicAuthenticationFilter 상세 설정이 필요한데 얘가 방해해서 정지
		JwtAuthenticationFilter basicAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager);
		basicAuthenticationFilter.setUserDetailsService(userDetailsService);
		http.addFilterAfter(basicAuthenticationFilter, OAuth2LoginAuthenticationFilter.class);
		return http.build();
	}


//	@Bean
//	public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) throws Exception {
//		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
//		builder.userDetailsService(userDetailsService)
//			.passwordEncoder(bCryptPasswordEncoder);
//		return builder.build();
//	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}


}
