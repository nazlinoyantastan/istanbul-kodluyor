package com.tobeto.entities.warehouse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
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

	@Column(nullable = true, name = "added_by_user")
	private String addedByUser;

	@Column(name = "is_deleted")
	private boolean isDeleted = false;

	@Column(nullable = true, name = "deleted_by_user")
	private String deletedByUser;

	@Column(name = "deleted_at")
	private LocalDateTime deletedAt;

	@OneToMany(mappedBy = "product")
	private List<ShelfProduct> shelfProducts;

}
