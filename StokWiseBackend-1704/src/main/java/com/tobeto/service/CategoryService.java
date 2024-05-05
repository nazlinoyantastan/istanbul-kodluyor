package com.tobeto.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tobeto.entities.warehouse.Category;
import com.tobeto.exception.ServiceException;
import com.tobeto.exception.ServiceException.ERROR_CODES;
import com.tobeto.repository.warehouse.CategoryRepository;

@Service
public class CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	public List<Category> getAllCategories() {
		return categoryRepository.findAll();
	}

	public Category addCategory(Category category) {
		return categoryRepository.save(category);
	}

	public void deleteCategory(UUID id) {
		Optional<Category> dbCategory = categoryRepository.findById(id);
		if (dbCategory.isPresent()) {
			categoryRepository.delete(dbCategory.get());
		} else {
			throw new ServiceException(ERROR_CODES.CATEGORY_NOT_FOUND);
		}

	}

	public void editCategory(UUID id, String name) {
		Optional<Category> oCategory = categoryRepository.findById(id);
		if (oCategory.isPresent()) {
			Category dbCategory = oCategory.get();
			dbCategory.setName(name);
			categoryRepository.save(dbCategory);
		} else {
			throw new ServiceException(ERROR_CODES.CATEGORY_NOT_FOUND);
		}

	}

}
