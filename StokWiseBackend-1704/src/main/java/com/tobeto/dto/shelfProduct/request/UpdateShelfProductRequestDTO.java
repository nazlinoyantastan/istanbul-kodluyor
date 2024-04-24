package com.tobeto.dto.shelfProduct.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShelfProductRequestDTO {

	private UUID id;
	private int capacity;
}