package com.tobeto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.tobeto.filter.JwtAuthorizationFilter;

@Configuration
public class SecurityConfig {
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		// @formatter:off
				http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers("/api/v1/login", "/api/v1/reportProduct", "/api/v1/reportProductWarningCount").permitAll()
						.anyRequest().authenticated()
						)
				     .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				     .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
				// @formatter:on
		return http.build();
	}

	@Bean
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("12345"));
	}
}