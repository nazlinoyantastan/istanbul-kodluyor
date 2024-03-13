package com.tobeto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FruitResponseDTO {
	private int id;
	private String name;
	private int minimum;
	private String image;
}