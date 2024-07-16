package za.co.yellowfire.threesixty.ui.component;

import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.notification.NotificationBuilder;
import org.springframework.data.mongodb.repository.MongoRepository;

public class MongoDbEntityPersistFunction<T> implements EntityPersistFunction<T> {

	private final MongoRepository<T, String> repository;

	public MongoDbEntityPersistFunction(final MongoRepository<T, String> repository) {
		this.repository = repository;
	}

	@Override
	public T apply(T entity) {
		try {
			return repository.save(entity);
		} catch (Throwable e) {
			NotificationBuilder.showNotification("Persist", e.getMessage());
		}
		return entity;
	}
}
