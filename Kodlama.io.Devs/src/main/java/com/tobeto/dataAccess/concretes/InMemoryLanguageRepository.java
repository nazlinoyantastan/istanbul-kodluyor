package com.tobeto.dataAccess.concretes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.tobeto.dataAccess.abstracts.LanguageRepository;
import com.tobeto.entities.concretes.Language;

@Repository
public class InMemoryLanguageRepository implements LanguageRepository {

	private List<Language> languages;

	public InMemoryLanguageRepository() {

		languages = new ArrayList<Language>();
		languages.add(new Language(1, "SQL"));
		languages.add(new Language(2, "Java"));
		languages.add(new Language(3, "C#"));
		languages.add(new Language(4, "Python"));

	}

	@Override
	public List<Language> getAll() {
		return languages;
	}

	@Override
	public Language getById(int id) {

		for (Language language : languages) {
			if (language.getId() == id) {
				return language;
			}
		}

		return null;

	}

	@Override
	public void update(int id, String name) {
		for (Language language : languages) {

			if (language.getName() != name && language.getId() == id) {
				language.setName(name);
			}

		}

	}

	@Override
	public void delete(int id) {
		Iterator<Language> iterator = languages.iterator();
		while (iterator.hasNext()) {
			Language language = iterator.next();
			if (language.getId() == id) {
				iterator.remove(); // Iterator üzerinden koleksiyonu güvenli bir şekilde silme
				System.out.println("Language deleted: ID=" + id);
				return;
			}
		}
		System.out.println("Language with ID=" + id + " not found.");
	}

	@Override
	public void add(Language language) {

		Iterator<Language> iterator = languages.iterator();

		while (iterator.hasNext()) {
			Language l = iterator.next();
			if (l.getId() == language.getId() || (l.getName() != null && l.getName().equals(language.getName()))) {

				System.out.println("Language with the same ID or name already exists. Not added.");
				return;
			}
		}

		languages.add(language);
		System.out.println("Language added: ID=" + language.getId() + ", Name=" + language.getName());
	}

}
