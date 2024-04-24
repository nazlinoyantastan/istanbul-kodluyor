package com.tobeto.dto.shelf.response;

import java.util.List;

import com.tobeto.dto.product.response.ProductResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShelfResponseDTO {

	private int id;
	private int capacity;
	private List<ProductResponseDTO> products;

}