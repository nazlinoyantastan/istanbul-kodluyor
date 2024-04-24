package com.tobeto.repository.warehouse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tobeto.entities.warehouse.ShelfProduct;

public interface ShelfProductRepository extends JpaRepository<ShelfProduct, UUID> {
	List<ShelfProduct> findByShelfId(UUID id);

	Optional<ShelfProduct> findFirstByProductId(UUID id);

	@Query("SELECT sp.product.id, SUM(sp.productCount) AS total_product_count, p.name FROM ShelfProduct sp INNER JOIN sp.product p GROUP BY sp.product.id, p.name ORDER BY sp.product.id")
	List<Object[]> getAllProductsFromShelves();

	@Query("SELECT sp.productCount FROM ShelfProduct sp WHERE sp.shelf.id = :shelfId AND sp.product.id = :productId")
	int findProductCountByShelfIdAndProductId(UUID shelfId, UUID productId);

	@Query("SELECT sp FROM ShelfProduct sp JOIN sp.shelf shelf WHERE sp.product.id = :productId GROUP BY sp.id, shelf.id HAVING SUM(sp.productCount) < MAX(shelf.capacity) ORDER BY SUM(sp.productCount) ASC LIMIT 1")
	Optional<ShelfProduct> findByProductIdNotFull(UUID productId);

	@Transactional
	@Modifying
	@Query("DELETE FROM ShelfProduct sp WHERE sp.shelf.id = :shelfId AND sp.product.id = :productId")
	void deleteProductFromShelf(UUID shelfId, UUID productId);

	@Query("SELECT sp FROM ShelfProduct sp WHERE sp.product.id = :productId AND sp.productCount > :count ORDER BY sp.productCount DESC")
	List<ShelfProduct> findByProductIdAndProductCountGreaterThan(UUID productId, int count);

}
