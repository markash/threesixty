package za.co.yellowfire.threesixty.ui.view;

import java.io.Serializable;

import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.FilterableTable;
import org.vaadin.viritin.fields.MTable;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.ui.component.FilterTextField;
import za.co.yellowfire.threesixty.ui.component.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;

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
	
	private final Class<T> beanType;
    private SpringEntityProvider<T> entityProvider;
    private MTable<T> table;
    private final String[] propertiesToFilterOn;
    
	protected abstract void onCreate(ClickEvent event);
	protected abstract void onTableIdClick(ClickEvent event, String value);
	
	protected abstract T buildEmpty();
	protected abstract String[] getTablePropertyNames();
	protected abstract String[] getTablePropertyHeaders();
	protected abstract Button[] getTableButtons();
	
	protected AbstractTableSearchView(final Class<T> beanType, final SpringEntityProvider<T> entityProvider, final String[] propertiesToFilterOn) {
		this.beanType = beanType;
		this.entityProvider = entityProvider;
		
		this.propertiesToFilterOn = propertiesToFilterOn;
		this.table = buildTable(); 
		build();
	}
	
	protected Component buildHeaderButtons() {
		if (propertiesToFilterOn != null) {
			return new HeaderButtons(new FilterTextField<T>(this.table, this.propertiesToFilterOn), getTableButtons());
		} else {
			return new HeaderButtons(getTableButtons());
		}
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
                .withColumnHeaders(getTablePropertyHeaders());
		
		table.addGeneratedColumn("id", new Table.ColumnGenerator() {	
			@Override
			public Object generateCell(Table source, Object itemId, Object columnId) {
				Item x = source.getItem(itemId);
				Property<String> id = x.getItemProperty("id");
				
				Button button = new Button(id.getValue());
				button.setStyleName("link");
				button.addClickListener(event -> { onTableIdClick(event, id.getValue()); } );
				return button;
			}
		});

		return table;
	}

	
	//protected void setRepository(final PagingAndSortingRepository<T, String> repository) { this.repository = repository; }
	//protected PagingAndSortingRepository<T, String> getRepository() { return this.repository; }
	protected MTable<T> getTable() { return this.table; }
}
