package com.tobeto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tobeto.entity.Fruit;

public interface FruitRepository extends JpaRepository<Fruit, Integer> {

}