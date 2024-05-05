package com.tobeto.repository.warehouse;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tobeto.entities.warehouse.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

	@Query("SELECT p FROM Product p ORDER BY p.name")
	List<Product> findAll();

}