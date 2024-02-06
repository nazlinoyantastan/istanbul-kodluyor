package com.tobeto.business.concretes;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tobeto.business.abstracts.LanguageService;
import com.tobeto.dataAccess.abstracts.LanguageRepository;
import com.tobeto.entities.concretes.Language;

@Service
public class LanguageManager implements LanguageService {

	private LanguageRepository languageRepository;

	public LanguageManager(LanguageRepository languageRepository) {
		this.languageRepository = languageRepository;
	}

	@Override
	public List<Language> getAll() {

		return languageRepository.getAll();
	}

	@Override
	public Language getById(int id) {
		return languageRepository.getById(id);
	}

	@Override
	public void update(int id, String name) {

		languageRepository.update(id, name);

	}

	@Override
	public void delete(int id) {
		languageRepository.delete(id);

	}

	@Override
	public void add(Language language) {
		languageRepository.add(language);

	}

}
