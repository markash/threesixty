package za.co.yellowfire.threesixty.domain.question;

import za.co.yellowfire.threesixty.domain.PersistenceException;

import java.util.Optional;

public interface Service<T> {
	Optional<T> findById(final String id);
	T save(final T object) throws PersistenceException;
	void delete(final T object);
}
