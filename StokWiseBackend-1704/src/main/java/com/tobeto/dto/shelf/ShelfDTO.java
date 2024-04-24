package com.tobeto.dto.shelf;

import java.util.List;
import java.util.UUID;

import com.tobeto.dto.product.ProductDTO;

import lombok.Data;

@Data
public class ShelfDTO {

	private UUID id;
	private int capacity;
	private List<ProductDTO> products;

}