package za.co.yellowfire.threesixty.domain.question;

import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.user.User;

public interface Service<T> {
	T findById(final String id);
	T save(final T object, final User changedBy) throws PersistenceException;
	void delete(final T object, final User changedBy);
}
