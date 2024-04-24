package com.tobeto.dto.shelf;

import java.util.List;

import com.tobeto.dto.product.ProductDTO;

import lombok.Data;

@Data
public class ShelfDTO {
	private int id;
	private int capacity;
	private List<ProductDTO> products;

}