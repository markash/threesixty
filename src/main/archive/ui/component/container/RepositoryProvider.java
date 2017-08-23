package za.co.yellowfire.threesixty.ui.component.container;

import java.io.Serializable;
import java.util.List;

public interface RepositoryProvider<T> extends Serializable {
	List<T> findEntities();
}
