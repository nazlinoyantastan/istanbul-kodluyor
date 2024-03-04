package com.tobeto.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.account.AccountRequestDTO;
import com.tobeto.service.KullaniciService;

@RestController
@RequestMapping("/api/v1")
public class HesapController {
	@Autowired
	private KullaniciService kullaniciService;

	@PostMapping("/sifreDegistir")
	public ResponseEntity<SuccessResponseDTO> sifreDegistir(Principal principal, @RequestBody AccountRequestDTO dto) {
		boolean sonuc = kullaniciService.sifreDegistir(dto.getEskiSifre(), dto.getYeniSifre(), principal.getName());
		if (sonuc) {
			return ResponseEntity.ok(new SuccessResponseDTO("şifre değiştirildi"));
		} else {
			return ResponseEntity.internalServerError().build();
		}

	}

}