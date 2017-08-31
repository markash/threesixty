package za.co.yellowfire.threesixty.domain.question;

import za.co.yellowfire.threesixty.domain.PersistenceException;

public interface Service<T> {
	T findById(final String id);
	T save(final T object) throws PersistenceException;
	void delete(final T object);
}
