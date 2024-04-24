package com.tobeto.dto.shelfProduct.request;

import com.tobeto.entities.warehouse.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryShelfShelfProductDTO {

	private int count;
	private Product product;
}