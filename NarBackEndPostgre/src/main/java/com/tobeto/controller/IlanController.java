package com.tobeto.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.yazilim.TumYazilimIlanlarResponseDTO;
import com.tobeto.dto.yazilim.YazilimIlanVerRequestDTO;
import com.tobeto.entity.YazilimIlan;
import com.tobeto.service.IlanService;

@RestController
@RequestMapping("/api/v1")
public class IlanController {
	@Autowired
	private IlanService ilanService;
	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;
	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@PostMapping("/yazilimIlanVer")
	public ResponseEntity<SuccessResponseDTO> yazilimIlanVer(@RequestBody YazilimIlanVerRequestDTO dto) {
		YazilimIlan yazilimIlan = requestMapper.map(dto, YazilimIlan.class);
		ilanService.yazilimIlanVer(yazilimIlan);
		return ResponseEntity.ok(new SuccessResponseDTO("ilan oluşturuldu"));
	}

	@GetMapping("/yazilimIlanlari")
	public ResponseEntity<List<TumYazilimIlanlarResponseDTO>> yazilimIlanlari() {
		List<YazilimIlan> tumIlanlar = ilanService.getTumYazilimIlanlari();
		List<TumYazilimIlanlarResponseDTO> sonuc = new ArrayList<>();
		tumIlanlar.forEach(ilan -> {
			sonuc.add(responseMapper.map(ilan, TumYazilimIlanlarResponseDTO.class));
		});

		return ResponseEntity.ok(sonuc);
		// return new ResponseEntity<List<TumYazilimIlanlarResponseDTO>>(sonuc,
		// HttpStatus.OK);
	}
}