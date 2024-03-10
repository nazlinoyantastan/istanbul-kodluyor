package com.tobeto.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	@Value("${SECRET_KEY:'Dan4gIt9bTcngt+W5iZcfj8NgSDKkeF8ZqEak3kSoHRebI9AkNSxSWmRZwlG+kXAjAJWuQy/mH6jCnmDOVehdA=='}")
	private String KEY;

	public String createToken(String email) {
		JwtBuilder builder = Jwts.builder();

		Instant tarih = Instant.now().plus(15, ChronoUnit.MINUTES);

		builder = builder.subject("login").id(email).issuedAt(new Date()).expiration(Date.from(tarih));

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

	public static void main(String[] args) {
		keyUret();
	}

	public static void keyUret() {
		SecretKey key = Jwts.SIG.HS512.key().build();
		String str = Encoders.BASE64.encode(key.getEncoded());
		System.out.println(str);
	}
}
