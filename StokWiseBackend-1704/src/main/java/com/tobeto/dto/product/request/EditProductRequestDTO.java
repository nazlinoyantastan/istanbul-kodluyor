package com.tobeto.dto.product.request;

import java.util.UUID;

import lombok.Data;

@Data
public class EditProductRequestDTO {

	private UUID id;
	private String name;
	private double price;
	private int quantity;
	private int unitInStock;
	private int minimumCount;
	private String description;
}
