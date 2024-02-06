package com.tobeto.webApi.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tobeto.business.abstracts.LanguageService;
import com.tobeto.entities.concretes.Language;

@RestController
@RequestMapping("/language")
public class LanguagesController {
	private LanguageService languageService;

	public LanguagesController(LanguageService languageService) {
		this.languageService = languageService;
	}

	@GetMapping("/getall")

	public List<Language> getAll() {

		return languageService.getAll();
	}

	@GetMapping("/{id}")
	public Language getById(@PathVariable("id") int id) {
		return languageService.getById(id);
	}

	@PutMapping("/update/{id}/{name}")
	public void update(@PathVariable("id") int id, @PathVariable("name") String name) {

		languageService.update(id, name);

	}

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable("id") int id) {
		languageService.delete(id);

	}

	@PostMapping("/add")
	public void add(@RequestBody Language language) {
		languageService.add(language);

	}
}
