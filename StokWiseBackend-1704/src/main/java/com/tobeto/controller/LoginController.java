package com.tobeto.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.login.LoginRequestDTO;
import com.tobeto.dto.login.LoginResponseDTO;
import com.tobeto.entities.user.User;
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

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
		Optional<User> optionalUser = userService.getUserByEmail(loginRequestDTO.getEmail());
		if (optionalUser.isPresent() && optionalUser.get().getPassword().equals(loginRequestDTO.getPassword())) {
			String token = tokenService.createToken(optionalUser.get());
			return ResponseEntity.ok(new LoginResponseDTO(token));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

}