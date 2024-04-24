package com.tobeto.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tobeto.entities.user.Role;
import com.tobeto.entities.user.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	@Value("${SECRET_KEY:'Dan4gIt9bTcngt+W5iZcfj8NgSDKkeF8ZqEak3kSoHRebI9AkNSxSWmRZwlG+kXAjAJWuQy/mH6jCnmDOVehdA=='}")
	private String KEY;

	public String createToken(User user) {
		List<Role> onDbRoles = user.getRoles();

		JwtBuilder builder = Jwts.builder();
		Instant tokenExp = Instant.now().plus(15, ChronoUnit.MINUTES);

		Map<String, Object> customKeys = new HashMap<>();
		List<String> roles = new ArrayList<>();

		for (Role role : onDbRoles) {
			roles.add(role.getName());
		} // onDbRoles.forEach(r -> roles.add(r.getName()));

		customKeys.put("roles", roles);

		builder = builder.claims(customKeys).subject("login")
				.id(user.getEmail()).issuedAt(new Date())
				.expiration(Date.from(tokenExp));

		return builder.signWith(getKey()).compact();
	}

	public Claims tokenKontrol(String token) {
		JwtParser builder = Jwts.parser().verifyWith(getKey()).build();
		return builder.parseSignedClaims(token).getPayload();
	}

	private SecretKey getKey() {
		SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY));
		return key;
	}
}