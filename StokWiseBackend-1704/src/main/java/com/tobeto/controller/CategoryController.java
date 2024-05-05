package com.tobeto.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.dto.SuccessResponseDTO;
import com.tobeto.dto.category.CategoryDTO;
import com.tobeto.dto.category.GetAllCategories;
import com.tobeto.dto.category.request.DeleteCategoryRequestDTO;
import com.tobeto.dto.category.request.UpdateCategoryRequestDTO;
import com.tobeto.entities.warehouse.Category;
import com.tobeto.service.CategoryService;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {

	@Autowired
	private CategoryService categoryService;

	@Autowired
	@Qualifier("requestMapper")
	private ModelMapper requestMapper;

	////
	@Autowired
	@Qualifier("responseMapper")
	private ModelMapper responseMapper;

	@GetMapping("/getAll") // ResponseEntity
	public ResponseEntity<List<GetAllCategories>> getAllProducts() {
		List<Category> allCategories = categoryService.getAllCategories();
		List<GetAllCategories> allCategoriesDTO = new ArrayList<>();
		allCategories.forEach(category -> {
			allCategoriesDTO.add(responseMapper.map(category, GetAllCategories.class));
		});
		return ResponseEntity.ok(allCategoriesDTO);
	}

	@PostMapping("/addCategory")
	public SuccessResponseDTO addCategory(@RequestBody CategoryDTO categoryDTO) {
		Category category = requestMapper.map(categoryDTO, Category.class);
		categoryService.addCategory(category);
		return new SuccessResponseDTO("Category created!");

	}

	@PostMapping("/deleteCategory")
	public SuccessResponseDTO deleteCategory(@RequestBody DeleteCategoryRequestDTO deleteCategoryRequestDTO) {
		categoryService.deleteCategory(deleteCategoryRequestDTO.getId());
		return new SuccessResponseDTO("Category deleted!");

	}

	@PostMapping("/editCategory")
	public SuccessResponseDTO editCategory(@RequestBody UpdateCategoryRequestDTO updateCategoryRequestDTO) {
		categoryService.editCategory(updateCategoryRequestDTO.getId(), updateCategoryRequestDTO.getName());
		return new SuccessResponseDTO("Category updated!");

	}

}