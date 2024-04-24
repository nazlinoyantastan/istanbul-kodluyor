package com.tobeto.dto.product.response;

import java.util.UUID;

import com.tobeto.dto.category.GetCategoryDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetAllProductsResponseDTO {

	private UUID id;
	private String name;
	private GetCategoryDTO category;
	private double price;
	private int quantity;
	private int unitInStock;
	private int minimumCount;
	private String description;
}
