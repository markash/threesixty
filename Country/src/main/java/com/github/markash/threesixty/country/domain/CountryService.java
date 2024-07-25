package com.github.markash.threesixty.country.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CountryService {

	private final CountryRepository repository;

	public CountryService(final CountryRepository repository) {
		this.repository = repository;
	}

	public Page<Country> findCountries(Pageable pageable) {
		return repository.findAll(pageable);
	}

}
