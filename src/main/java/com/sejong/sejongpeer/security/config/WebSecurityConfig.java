package com.sejong.sejongpeer.security.config;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.security.config.Customizer.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.sejong.sejongpeer.global.common.constants.UrlConstants;
import com.sejong.sejongpeer.global.util.SpringEnvironmentUtil;
import com.sejong.sejongpeer.security.constant.WebSecurityURIs;
import com.sejong.sejongpeer.security.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final SpringEnvironmentUtil springEnvironmentUtil;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();

		// configuration.setAllowedOrigins(WebSecurityURIs.CORS_ALLOW_URIS);
		configuration.addAllowedOriginPattern(UrlConstants.DEV_URL.getValue());
		configuration.addAllowedOriginPattern(UrlConstants.WWW_DEV_URL.getValue());
		configuration.addAllowedOriginPattern(UrlConstants.LOCAL_DOMAIN_URL.getValue());
		configuration.addAllowedOriginPattern(UrlConstants.LOCAL_SECURE_DOMAIN_URL.getValue());
		configuration.addAllowedOriginPattern(UrlConstants.DEV_NONE_SECURE_URL.getValue());
		configuration.addAllowedOriginPattern(UrlConstants.WWW_DEV_NONE_SECURE_URL.getValue());
		configuration.addAllowedOriginPattern(UrlConstants.SEJONG_AUTH_API_URL.getValue());
		configuration.addAllowedMethod("*");
		configuration.addAllowedHeader("*");
		configuration.setAllowCredentials(true);
		configuration.addExposedHeader(SET_COOKIE);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}

	private void defaultFilterChain(HttpSecurity http) throws Exception {
		http.httpBasic(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.cors(withDefaults())
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http)
		throws Exception { // TODO: 추후 프로필에 따라 접근 권한 변경 필요
		defaultFilterChain(http);

		http.authorizeHttpRequests(
				authorize ->
					authorize
						.requestMatchers("/sejongpeer-actuator/**")
						.permitAll()
						.requestMatchers(WebSecurityURIs.PUBLIC_URIS.toArray(String[]::new))
						.permitAll()
						.requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**")
						.permitAll()
						.anyRequest()
						.authenticated())
			.sessionManagement(
				session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.anonymous(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.logout(AbstractHttpConfigurer::disable)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		// TODO: 추후 적용 필요

		return http.build();
	}
}
