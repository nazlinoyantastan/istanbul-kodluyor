package com.tobeto.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Kullanicilar;
import com.tobeto.entity.Roller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenService {
	@Value("${SECRET_KEY}")
	private String KEY;

	public String createToken(Kullanicilar kullanicilar) {
		JwtBuilder builder = Jwts.builder();

		List<Roller> rollerKullanici = kullanicilar.getRollers();

		String[] roller = new String[rollerKullanici.size()];
		for (int i = 0; i < rollerKullanici.size(); i++) {
			roller[i] = rollerKullanici.get(i).getRol();
		}

		// add custom keys
		Map<String, Object> customKeys = new HashMap<>();
		customKeys.put("roller", roller);
		builder = builder.claims(customKeys);

//		Instant tarih = Instant.now().plus(15, ChronoUnit.MINUTES);
		Instant tarih = Instant.now().plus(1, ChronoUnit.SECONDS);

		builder = builder.subject("login").id(kullanicilar.getKullaniciAdi()).issuedAt(new Date())
				.expiration(Date.from(tarih));

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