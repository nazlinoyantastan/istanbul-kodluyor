package com.tobeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.login.LoginRequestDTO;
import com.tobeto.dto.login.LoginResponseDTO;
import com.tobeto.service.LoginService;

@RestController
@RequestMapping("/api/v1")
public class LoginController {
	@Autowired
	private LoginService loginService;

	@PostMapping("/login")
	public LoginResponseDTO login(@RequestBody LoginRequestDTO dto) {
		String token = loginService.login(dto.getEmail(), dto.getPassword());
		LoginResponseDTO responseDTO = new LoginResponseDTO();
		responseDTO.setToken(token);
		return responseDTO;
	}
}
