package com.tobeto.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
	@Autowired
	private TokenService tokenService;

	public String login(String email, String password) {
		if (email.equals(password)) {
			String token = tokenService.createToken(email);
			return token;
		} else {
			throw new RuntimeException("Login Error");
		}
	}
}
