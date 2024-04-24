package com.tobeto.dto.product.request;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntryProductRequestDTO {
	private UUID productId;
	private int count;

}
