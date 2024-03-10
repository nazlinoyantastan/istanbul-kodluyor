package com.tobeto.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Member;
import com.tobeto.entity.Role;
import com.tobeto.repository.RoleRepository;

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

	@Autowired
	private RoleRepository roleRepository;

	public String createToken(Member user) {
		List<Role> dbRoller = roleRepository.findByMemberId(user.getId());

		JwtBuilder builder = Jwts.builder();

		Instant tarih = Instant.now().plus(15, ChronoUnit.MINUTES);
		Map<String, Object> customKeys = new HashMap<String, Object>();

		List<String> roller = new ArrayList<String>();

//      dbRoller.forEach(r -> roller.add(r.getRoleName()));

//      for (Role r : dbRoller) {
//          roller.add(r.getRoleName());
//      }

		for (int i = 0; i < dbRoller.size(); i++) {
			roller.add(dbRoller.get(i).getRoleName());
		}

		customKeys.put("roller", roller);

		builder = builder.claims(customKeys).subject("login").id(user.getEmail()).issuedAt(new Date())
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

	public static void main(String[] args) {
		keyUret();
	}

	public static void keyUret() {
		SecretKey key = Jwts.SIG.HS512.key().build();
		String str = Encoders.BASE64.encode(key.getEncoded());
		System.out.println(str);
	}
}
