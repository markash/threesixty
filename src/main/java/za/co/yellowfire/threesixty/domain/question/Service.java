package za.co.yellowfire.threesixty.domain.question;

import za.co.yellowfire.threesixty.domain.user.User;

public interface Service<T> {
	T save(final T object, final User changedBy);
	void delete(final T object, final User changedBy);
}
