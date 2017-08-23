package za.co.yellowfire.threesixty.ui.component.container;

import org.vaadin.viritin.v7.ListContainer;

import java.util.ArrayList;

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
