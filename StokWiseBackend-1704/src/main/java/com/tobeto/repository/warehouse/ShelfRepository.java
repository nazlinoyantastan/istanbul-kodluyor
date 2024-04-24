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

//	@Query("SELECT s FROM Shelf s JOIN s.products p WHERE p.id = :productID AND s.productCount < s.capacity")
//	Optional<Shelf> findByProductIdNotFull(int productID);

////	@Query("SELECT SUM(s.productCount) FROM Shelf s JOIN s.product p WHERE p.id = :productID")
////	Integer getProductCount(int productID);

////	@Query("SELECT SUM(p.quantity) FROM Shelf s JOIN s.products p WHERE p.id = :productID")
////	Integer getProductCount(@Param("productID") int productID);

////	@Query("SELECT SUM(s.productCount) FROM Shelf s JOIN s.products p WHERE p.id = :productID")
////	Integer getProductCount(int productID);

}
