package com.tobeto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.signup.SignupRequestDTO;
import com.tobeto.dto.signup.SignupResponseDTO;
import com.tobeto.entities.user.User;
import com.tobeto.service.LoginService;
import com.tobeto.service.TokenService;

@RestController
@RequestMapping("/api/v1")
public class SignUpController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private TokenService tokenService;

	@PostMapping("/user/signup")
	public ResponseEntity<SignupResponseDTO> userSignUp(@Validated @RequestBody SignupRequestDTO signupRequestDTO) {

		User user = loginService.userSignUp(signupRequestDTO.getEmail(), signupRequestDTO.getPassword());
		String token = tokenService.createToken(user);
		return ResponseEntity.ok(new SignupResponseDTO(token));
	}

	// Entegre edilecek
//	@PostMapping("/admin/signup")
//	public ResponseEntity<SignupResponseDTO> adminSignUp(
//			@RequestBody SignupRequestDTO signupRequestDTO) {
//		String token = loginService.adminSignUp(signupRequestDTO.getEmail(),
//				signupRequestDTO.getPassword());
//		return ResponseEntity.ok(new SignupResponseDTO(token));
//	}
}