package com.tobeto.entities.warehouse;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Product {

	@Id
	@GeneratedValue
	private UUID id;
	private String name;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;

	private double price;
	private int quantity; // O ürünün depodaki toplam adedi
	private int unitInStock; // Ürünün o anki raflara yerleşmiş miktarı
	private int minimumCount; // Uyarı verecek miktarı
	private String description;

	private boolean isDeleted = false;

	@OneToMany(mappedBy = "product")
	private List<ShelfProduct> shelfProducts;

}
