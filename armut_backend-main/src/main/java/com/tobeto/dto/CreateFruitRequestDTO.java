package com.tobeto.dto;

import lombok.Data;

@Data
public class CreateFruitRequestDTO {
	private String name;
	private int minimum;
	private String image;
}