package com.github.markash.threesixty.web.service;

import com.github.markash.threesixty.web.Person;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@BrowserCallable
@AnonymousAllowed
public class UserService {

	private final Person person = new Person("Mark", "Ashworth");

	public Optional<Person> currentUser() {
		return Optional.of(person);
	}
}
