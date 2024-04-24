package com.tobeto.repository.warehouse;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entities.warehouse.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
