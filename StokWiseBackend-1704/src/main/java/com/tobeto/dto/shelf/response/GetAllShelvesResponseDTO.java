package com.tobeto.dto.shelf.response;

import lombok.Data;

@Data
public class GetAllShelvesResponseDTO {
	private int id;
	private int productCount;
	private int capacity;
	private String productCategory;
	private String productName;

}
