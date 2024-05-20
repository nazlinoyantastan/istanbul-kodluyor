package com.tobeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.login.LoginRequestDTO;
import com.tobeto.dto.login.LoginResponseDTO;
import com.tobeto.service.LoginService;
import com.tobeto.service.TokenService;
import com.tobeto.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

	@Autowired
	private UserService userService;

	@Autowired
	private TokenService tokenService;

	@Autowired
	private LoginService loginService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {

		try {
			String token = loginService.login(loginRequestDTO.getEmail(), loginRequestDTO.getPassword());
			return ResponseEntity.ok(new LoginResponseDTO(token));
		} catch (RuntimeException ex) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

}