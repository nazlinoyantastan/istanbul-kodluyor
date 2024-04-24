package com.tobeto.entities.warehouse;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Shelf {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int capacity;

	@OneToMany(mappedBy = "shelf")
	private List<ShelfProduct> shelfProducts;

}
