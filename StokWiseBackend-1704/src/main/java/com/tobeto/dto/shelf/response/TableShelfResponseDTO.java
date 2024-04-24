package com.tobeto.dto.shelf.response;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableShelfResponseDTO {

	private UUID id;
	private int productCount;
	private int capacity;
	private String productCategory;

}