package com.tobeto.dto.shelfProduct.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllProductsFromShelvesResponseDTO {

	private int id;
	private long unitInStock;
	private String name;

}