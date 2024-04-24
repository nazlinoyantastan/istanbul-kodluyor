package com.tobeto.dto.product.request;

import lombok.Data;

@Data
public class EditProductRequestDTO {
	private int id;
	private String name;
	private double price;
	private int quantity;
	private int unitInStock;
	private int minimumCount;
	private String description;
}
