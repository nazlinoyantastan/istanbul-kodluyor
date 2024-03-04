package com.tobeto.controller;

import java.util.Optional;

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
import com.tobeto.entity.Kullanicilar;
import com.tobeto.service.KullaniciService;
import com.tobeto.service.TokenService;

@RestController
@RequestMapping("/api/v1")
public class LoginController {
	@Autowired
	private KullaniciService kullaniciService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
		Optional<Kullanicilar> oKullanicilar = kullaniciService.getKullanici(dto.getEmail());
		if (oKullanicilar.isPresent() && passwordEncoder.matches(dto.getPassword(), oKullanicilar.get().getSifre())) {
			String token = tokenService.createToken(oKullanicilar.get());
			return ResponseEntity.ok(new LoginResponseDTO(token));
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

}