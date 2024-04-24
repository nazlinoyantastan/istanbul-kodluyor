package com.tobeto.repository.warehouse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tobeto.entities.warehouse.Shelf;

public interface ShelfRepository extends JpaRepository<Shelf, Integer> {
//	// Product sayısına göre tüm Shelfleri getiriyor.
//	List<Shelf> findAllByProductCount(int count);
//
//	// Shelf'e (raf) eklenmek istenilen ürün, daha önceden herhangi bir rafa
//	// eklenmiş
//	// mi? Sorgusunu çağıran method.
//	List<Shelf> findAllByProductsIdAndProductCountGreaterThan(int id, int count);
//
	List<Shelf> findByShelfProductsProductCategoryId(int id);

//	@Query("SELECT s FROM Shelf s JOIN fetch s.shelfProducts p Where SUM(p.productCount) = 0")
//	List<Shelf> findEmptyShelves();

	@Query("SELECT s.id FROM Shelf s LEFT JOIN ShelfProduct sp ON s.id = sp.shelf.id WHERE sp.id IS NULL")
	List<Integer> findEmptyShelves();

////	@Query("SELECT s FROM Shelf s Where s.product.id = :productID and s.productCount < s.capacity")
////	Optional<Shelf> findByFruitIdNotFull(int productID);
//
//	@Query("SELECT s FROM Shelf s JOIN s.products p WHERE p.id = :productID AND s.productCount < s.capacity")
//	Optional<Shelf> findByProductIdNotFull(int productID);
//
////	@Query("SELECT SUM(s.productCount) FROM Shelf s JOIN s.product p WHERE p.id = :productID")
////	Integer getProductCount(int productID);
////	
////	@Query("SELECT SUM(p.quantity) FROM Shelf s JOIN s.products p WHERE p.id = :productID")
////	Integer getProductCount(@Param("productID") int productID);
//
////	@Query("SELECT SUM(s.productCount) FROM Shelf s JOIN s.products p WHERE p.id = :productID")
////	Integer getProductCount(int productID);

}
