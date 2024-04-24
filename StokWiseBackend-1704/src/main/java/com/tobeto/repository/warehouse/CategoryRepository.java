package com.tobeto.repository.warehouse;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entities.warehouse.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

}
