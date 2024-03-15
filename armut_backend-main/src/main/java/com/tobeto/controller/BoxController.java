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

import com.tobeto.dto.BoxResponseDTO;
import com.tobeto.dto.CreateBoxRequestDTO;
import com.tobeto.dto.DeleteBoxRequestDTO;
import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.UpdateBoxRequestDTO;
import com.tobeto.entity.Box;
import com.tobeto.service.BoxService;

@RestController
@RequestMapping("/api/v1/box")
public class BoxController {
	@Autowired
	private BoxService boxService;
	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@GetMapping("/")
	public List<BoxResponseDTO> getAllBoxes() {
		List<Box> boxes = boxService.getAllBoxes();
		return boxes.stream().map(b -> responseMapper.map(b, BoxResponseDTO.class)).toList();
	}

	@PostMapping("/create")
	public SuccessResponseDTO createBoxes(@RequestBody CreateBoxRequestDTO dto) {
		int count = boxService.createBoxes(dto.getCapacity(), dto.getCount());
		return new SuccessResponseDTO(String.valueOf(count));
	}

	@PostMapping("/delete")
	public SuccessResponseDTO deleteBox(@RequestBody DeleteBoxRequestDTO dto) {
		boxService.deleteBox(dto.getId());
		return new SuccessResponseDTO();
	}

	@PostMapping("/update")
	public SuccessResponseDTO updateBox(@RequestBody UpdateBoxRequestDTO dto) {
		boxService.updateBox(dto.getId(), dto.getCapacity());
		return new SuccessResponseDTO();
	}
}