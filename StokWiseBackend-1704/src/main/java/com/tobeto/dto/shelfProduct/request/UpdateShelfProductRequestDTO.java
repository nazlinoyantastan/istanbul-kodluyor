package com.tobeto.dto.shelfProduct.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShelfProductRequestDTO {

	private int id;
	private int capacity;
}