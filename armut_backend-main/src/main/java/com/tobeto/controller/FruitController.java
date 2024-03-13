package com.tobeto.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.CreateFruitRequestDTO;
import com.tobeto.dto.FruitResponseDTO;
import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.entity.Fruit;
import com.tobeto.service.FruitService;

@RestController
@RequestMapping("/api/v1/fruit")
public class FruitController {
	@Autowired
	private FruitService fruitService;
	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@PostMapping("/create")
	public SuccessResponseDTO createFruit(@RequestBody CreateFruitRequestDTO dto) {
		Fruit fruit = requestMapper.map(dto, Fruit.class);
		fruitService.createFruit(fruit);
		return new SuccessResponseDTO();
	}

	@GetMapping("/")
	public List<FruitResponseDTO> getAllFruits() {
		List<Fruit> fruits = fruitService.getAllFruits();
		return fruits.stream().map(f -> responseMapper.map(f, FruitResponseDTO.class)).toList();
	}
}