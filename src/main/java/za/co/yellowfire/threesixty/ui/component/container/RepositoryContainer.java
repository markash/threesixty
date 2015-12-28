package za.co.yellowfire.threesixty.ui.component.container;

import java.util.ArrayList;

import org.vaadin.viritin.ListContainer;

@SuppressWarnings("serial")
public class RepositoryContainer<T> extends ListContainer<T> {

	private final RepositoryProvider<T> provider;
	
	public RepositoryContainer(final RepositoryProvider<T> provider) {
		super(new ArrayList<T>());
		this.provider = provider;
	}
	
	public void refresh() {
		removeAllItems();
		for (T item : this.provider.findEntities()) {
			addItem(item);
		}
	}
}
