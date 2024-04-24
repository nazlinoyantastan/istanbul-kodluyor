package com.tobeto.dto.product.request;

import com.tobeto.entities.warehouse.Category;

import lombok.Data;

@Data
public class AddProductRequestDTO {
	private String name;
	private Category category;
	private double price;
	private int quantity;
	private int unitInStock;
	private int minimumCount;
	private String description;
}
