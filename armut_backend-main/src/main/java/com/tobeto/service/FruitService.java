package com.tobeto.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Fruit;
import com.tobeto.repository.FruitRepository;

@Service
public class FruitService {
	@Autowired
	private FruitRepository fruitRepository;

	public Fruit createFruit(Fruit fruit) {
		return fruitRepository.save(fruit);
	}

	public List<Fruit> getAllFruits() {
		return fruitRepository.findAll();
	}

	public void deleteFruit(int id) {
		fruitRepository.deleteById(id);
	}
}