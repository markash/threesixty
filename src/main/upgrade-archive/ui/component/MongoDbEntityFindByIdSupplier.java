package za.co.yellowfire.threesixty.ui.component;

import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.component.notification.NotificationBuilder;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.io.Serializable;
import java.util.Optional;

public class MongoDbEntityFindByIdSupplier<T> implements EntitySupplier<T, Serializable> {
	private final MongoRepository<T, String> repository;

	public MongoDbEntityFindByIdSupplier(final MongoRepository<T, String> repository) {
		this.repository = repository;
	}

	@Override
	public Optional<T> get(final Serializable id) {
		try {
			return repository.findById((String) id);
		} catch (Throwable e) {
			NotificationBuilder.showNotification("Find", e.getMessage());
		}
		return Optional.empty();
	}
}
