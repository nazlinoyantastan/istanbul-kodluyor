package com.tobeto.dto.shelf.request;

import java.util.UUID;

import lombok.Data;

@Data
public class EditShelfRequestDTO {
	private UUID id;
	private int capacity;
}
