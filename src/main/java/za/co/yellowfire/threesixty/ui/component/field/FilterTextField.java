package za.co.yellowfire.threesixty.ui.component.field;

import org.apache.commons.lang3.StringUtils;
import org.vaadin.viritin.fields.MTable;

import com.vaadin.data.Container;
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
	private final PropertyTextFilter filter;
	
	@SuppressWarnings("serial")
	public FilterTextField(final MTable<T> table, final String[] propertiesToFilterOn) {
		super();
		
		this.table = table;
		filter = new PropertyTextFilter(propertiesToFilterOn);
		
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
	
	public void onFilter(final TextChangeEvent event) {
		filter.setFilter(event.getText());
		
		Filterable data = (Filterable) table.getContainerDataSource();
        data.removeContainerFilter(filter);
        data.addContainerFilter(filter);
	}
		
	@SuppressWarnings("serial")
	public static class PropertyTextFilter implements Container.Filter {
		
		private String filter = null;
		private String[] properties;
		
		public PropertyTextFilter(final String[] properties) {
			this.properties = properties;
		}
		
		public void setFilter(final String filter) { this.filter = filter; }
		
    	public boolean passesFilter(
    			final Object itemId,
                final Item item) {

            if (!StringUtils.isNoneBlank(this.filter)) {
                return true;
            }

            boolean passes = false;
            for(String property : properties) {
            	passes = passes || filterByProperty(property, item, this.filter);
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
}
