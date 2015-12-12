package za.co.yellowfire.threesixty.domain.user;

import java.util.List;

public interface RepositoryCustom<T> {
	List<T> findAddExcept(final T object);
}
