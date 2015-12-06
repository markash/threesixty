package za.co.yellowfire.threesixty.ui.component;

import org.vaadin.viritin.fields.MTable;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

public class FilterTextField<T> extends TextField {
	private static final long serialVersionUID = 1L;

	private final MTable<T> table;
	private final String[] properties;
	
	@SuppressWarnings("serial")
	public FilterTextField(final MTable<T> table, final String[] propertiesToFilterOn) {
		super();
		
		this.table = table;
		this.properties = propertiesToFilterOn;
		
        addTextChangeListener(this::onFilter);
        
        setInputPrompt("Filter");
        setIcon(FontAwesome.SEARCH);
        addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
        addShortcutListener(new ShortcutListener("Clear", KeyCode.ESCAPE, null) {
        	@Override
            public void handleAction(final Object sender, final Object target) {
                setValue("");
                ((Filterable) table.getContainerDataSource()).removeAllContainerFilters();
            }
        });
	}
	
	@SuppressWarnings("serial")
	public void onFilter(final TextChangeEvent event) {
		Filterable data = (Filterable) table.getContainerDataSource();
        data.removeAllContainerFilters();
        data.addContainerFilter(new Filter() {
        	public boolean passesFilter(
        			final Object itemId,
                    final Item item) {

                if (event.getText() == null
                        || event.getText().equals("")) {
                    return true;
                }

                boolean passes = false;
                for(String property : properties) {
                	passes = passes || filterByProperty(property, item, event.getText());
                }
                return passes;
            }

            @Override
            public boolean appliesToProperty(final Object propertyId) {
            	boolean applies = false;
            	for(String property : properties) {
            		applies = applies || propertyId.equals(property);
            	}
                return applies;
            }
        });
	}
	
	private boolean filterByProperty(final String prop, final Item item,
            final String text) {
        if (item == null || item.getItemProperty(prop) == null
                || item.getItemProperty(prop).getValue() == null) {
            return false;
        }
        String val = item.getItemProperty(prop).getValue().toString().trim()
                .toLowerCase();
        if (val.contains(text.toLowerCase().trim())) {
            return true;
        }
        return false;
    }
}
