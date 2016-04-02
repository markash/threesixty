package za.co.yellowfire.threesixty.ui.view;

import java.io.Serializable;
import java.util.Locale;

import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.FilterableTable;
import org.vaadin.viritin.fields.MTable;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;
import za.co.yellowfire.threesixty.ui.component.button.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.field.FilterTextField;

/**
 * Abstract table search view
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class AbstractTableSearchView<T, ID extends Serializable> extends AbstractDashboardPanel {
	private static final long serialVersionUID = 1L;

	protected static final String BUTTON_OK = "Save";
	protected static final String BUTTON_RESET = "Reset";
	protected static final String BUTTON_DELETE = "Delete";
	protected static final String BUTTON_ADD = "New";
	protected static final String BUTTON_VIEW = "View";
	protected static final String DEFAULT_LINK_PROPERTY_ID = "id";
	protected static final String DEFAULT_LINK_PROPERTY_TEXT = "id";
	
	private final Class<T> beanType;
    private SpringEntityProvider<T> entityProvider;
    private MTable<T> table;
    private final String[] propertiesToFilterOn;
    private Component[] headerComponents;
    private HeaderButtons headerButtons;
    
	protected abstract void onCreate(ClickEvent event);
	protected abstract void onTableIdClick(ClickEvent event, String value);
	
	protected abstract T buildEmpty();
	protected abstract String[] getTablePropertyNames();
	protected abstract String[] getTablePropertyHeaders();
	protected Component[] getTableButtons() { return this.headerComponents; }
	
	/**
	 * Returns the property (i.e. column) that should be made into a link to navigate to the detail and the actual id
	 * value is determined by the getLinkPropertyValue() property
	 * @return The table property name which is by default "id"
	 */
	protected String getLinkPropertyId() { return DEFAULT_LINK_PROPERTY_ID; }
	/**
	 * Returns the property that should be the href value of the link
	 * @return The table property name which is by default "id"
	 */
	protected String getLinkPropertyText() { return DEFAULT_LINK_PROPERTY_TEXT; }
	
	/**
	 * Constructs a search view for a class that is provided by an EntityProvider and that
	 * can be filtered on by an array of properties.
	 * 
	 * @param beanType The class of the entity class bean
	 * @param entityProvider The entity provider for the class
	 * @param propertiesToFilterOn The array of properties to fiter on
	 */
	protected AbstractTableSearchView(
			final Class<T> beanType, 
			final SpringEntityProvider<T> entityProvider, 
			final String[] propertiesToFilterOn,
			final Component...headerComponents) {
		
		this.beanType = beanType;
		this.entityProvider = entityProvider;
		this.headerComponents = headerComponents;
		
		this.propertiesToFilterOn = propertiesToFilterOn;
		this.table = buildTable(); 
		build();
	}
	
	protected Component getHeaderButtons() {
		if (propertiesToFilterOn != null) {
			this.headerButtons = new HeaderButtons(HeaderButtons.combine(new FilterTextField<T>(this.table, this.propertiesToFilterOn), getTableButtons()));
		} else {
			this.headerButtons = new HeaderButtons(getTableButtons());
		}
		return this.headerButtons;
	}
	
	protected void addHeaderComponent(final Component component) {
		this.headerButtons.addComponent(component, 1);
	}
	
	protected Component buildContent() {

		VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        //HorizontalLayout layout = new HorizontalLayout();
        //layout.setSpacing(true);
        //layout.setMargin(false);
        //layout.setSizeFull();
        //root.addComponent(layout);
        //root.setComponentAlignment(layout, Alignment.TOP_LEFT);
        
        this.table.setWidth(100f, Unit.PERCENTAGE);
        root.addComponent(this.table);
        //layout.setExpandRatio(this.table, 7f);
        
        //VerticalLayout formLayout = new VerticalLayout();
        //formLayout.setWidth(100f, Unit.PERCENTAGE);
        //formLayout.setStyleName(Style.Rating.PANEL);
        //formLayout.setSpacing(false);
        //formLayout.setMargin(false);
        //formLayout.setSizeFull();
        
            
        //layout.addComponent(formLayout);
        //layout.setExpandRatio(formLayout, 5f);

        return root;
    }
	
	@SuppressWarnings({"unchecked", "serial"})
	private MTable<T> buildTable() {
			
		MTable<T> table = new FilterableTable<T>(beanType)
				.setBeans(new SortableLazyList<T>(entityProvider, entityProvider, 100))
                .withProperties(getTablePropertyNames())
                .withColumnHeaders(getTablePropertyHeaders())
                ;
		
		
		
		table.addGeneratedColumn(getLinkPropertyText(), new Table.ColumnGenerator() {	
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Item x = source.getItem(itemId);
				
				Property<String> linkId = x.getItemProperty(getLinkPropertyId());
				Property<?> linkText = x.getItemProperty(getLinkPropertyText());
				Converter<String, Object> linkTextConverter = table.getConverter(getLinkPropertyText());
				
				String text = "";
				if (linkText.getValue() != null) {
					if (linkTextConverter != null) {
						text = linkTextConverter.convertToPresentation(linkText.getValue(), String.class, Locale.getDefault());
					} else {
						text = linkText.getValue().toString();
					}
				} else {
					text = "null";
				}
				
				Button button = new Button(text);
				button.setStyleName("link");
				button.addClickListener(event -> { onTableIdClick(event, linkId.getValue()); } );
				return button;
			}
		});

		return table;
	}

	
	//protected void setRepository(final PagingAndSortingRepository<T, String> repository) { this.repository = repository; }
	//protected PagingAndSortingRepository<T, String> getRepository() { return this.repository; }
	protected MTable<T> getTable() { return this.table; }
	
	protected void addFilter(final Filter filter) {
		((FilterableTable<T>)this.table).addFilter(filter);
	}
}
