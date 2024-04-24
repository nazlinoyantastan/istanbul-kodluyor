package com.tobeto.entities.warehouse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@Table(name = "shelf_product")
@NoArgsConstructor
@AllArgsConstructor
public class ShelfProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private int productCount;

	@ManyToOne
	private Product product;

	@ManyToOne
	private Shelf shelf;

}
