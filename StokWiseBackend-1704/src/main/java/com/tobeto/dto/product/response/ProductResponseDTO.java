package com.tobeto.dto.product.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDTO {

	private UUID id;
	private String name;
	private String category;
	private int count;

}