package com.tobeto.dto.shelfProduct.response;

import com.tobeto.dto.product.response.ProductResponseDTO;
import com.tobeto.dto.shelf.response.ShelfResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllShelvesShelfProductDTO {

	private int id;
	private ShelfResponseDTO shelf;
	private int productCount;
	private ProductResponseDTO product;

}