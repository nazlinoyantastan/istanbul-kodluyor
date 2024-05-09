package com.tobeto.repository.warehouse;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tobeto.entities.warehouse.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, UUID> {

	List<Shelf> findByShelfProductsProductCategoryId(UUID id);

	@Query("SELECT s.id FROM Shelf s LEFT JOIN ShelfProduct sp ON s.id = sp.shelf.id WHERE sp.id IS NULL")
	List<UUID> findEmptyShelves();

}
