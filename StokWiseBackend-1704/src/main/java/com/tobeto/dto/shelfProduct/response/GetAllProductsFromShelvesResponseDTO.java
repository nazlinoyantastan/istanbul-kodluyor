package com.tobeto.dto.shelfProduct.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllProductsFromShelvesResponseDTO {

	private UUID id;
	private long unitInStock;
	private String name;

}