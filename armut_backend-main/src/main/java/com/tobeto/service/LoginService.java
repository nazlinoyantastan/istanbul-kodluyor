package com.tobeto.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Member;

@Service
public class LoginService {
	@Autowired
	private TokenService tokenService;

	@Autowired
	private UserService userService;

	/*
	 * public String login(String email, String password) { if
	 * (email.equals(password)) { String token = tokenService.createToken(email);
	 * return token; } else { throw new RuntimeException("Login Error"); } }
	 */

	public String login(String email, String password) {
		Optional<Member> oUser = userService.getUserByEmail(email);
		if (oUser.isPresent() && oUser.get().getPassword().equals(password)) {
			String token = tokenService.createToken(email);
			return token;
		} else {
			throw new RuntimeException("Login Error");
		}
	}

	public String signup(String email, String password) {
		Member user = new Member();
		user.setEmail(email);
		user.setPassword(password);
		userService.createUser(user);
		return tokenService.createToken(email);
	}
}
