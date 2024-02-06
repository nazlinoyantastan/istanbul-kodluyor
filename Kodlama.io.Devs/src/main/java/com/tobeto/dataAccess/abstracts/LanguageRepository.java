package com.tobeto.dataAccess.abstracts;

import java.util.List;

import com.tobeto.entities.concretes.Language;

public interface LanguageRepository {

	List<Language> getAll();

	Language getById(int id);

	void update(int id, String name);

	void delete(int id);

	void add(Language language);

}
