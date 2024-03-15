package com.tobeto.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entity.Box;
import com.tobeto.repository.BoxRepository;

@Service
public class BoxService {
	@Autowired
	private BoxRepository boxRepository;

	public List<Box> getAllBoxes() {
		return boxRepository.findAll();
	}

	public int createBoxes(int capacity, int count) {
		if (count > 50) {
			count = 50;
		}
		for (int i = 0; i < count; i++) {
			Box box = new Box();
			box.setCapacity(capacity);
			boxRepository.save(box);
		}
		return count;
	}

	public void deleteBox(int id) {
		boxRepository.deleteById(id);
	}

	public void updateBox(int id, int capacity) {
		Optional<Box> oBox = boxRepository.findById(id);
		if (oBox.isPresent()) {
			Box box = oBox.get();
			box.setCapacity(capacity);
			boxRepository.save(box);
		}

	}
}