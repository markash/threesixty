package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.MTable;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;
import za.co.yellowfire.threesixty.ui.component.button.FormButtons;

import java.io.Serializable;

/**
 * Abstract table editor view
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class AbstractTableEditorView<T, ID extends Serializable> extends AbstractDashboardPanel {
	private static final long serialVersionUID = 1L;

    private Button saveButton = ButtonBuilder.SAVE(this::save, ValoTheme.BUTTON_PRIMARY);
    private Button resetButton = ButtonBuilder.RESET(this::reset, ValoTheme.BUTTON_DANGER);
    private Button newButton = ButtonBuilder.NEW(this::add, ValoTheme.BUTTON_FRIENDLY);
    private Button deleteButton = ButtonBuilder.DELETE(this::delete, ValoTheme.BUTTON_DANGER);
    private FormButtons buttonLayout = new FormButtons(saveButton, resetButton, newButton, deleteButton);
    
    private final Class<T> beanType;
    private final BeanFieldGroup<T> fieldGroup;
    private PagingAndSortingRepository<T, String> repository;
    private MTable<T> table;
    
	protected abstract void save(ClickEvent event);
	protected abstract void add(ClickEvent event);
	protected abstract void reset(ClickEvent event);
	protected abstract void delete(ClickEvent event);
	protected abstract T buildEmpty();
	protected abstract Layout buildForm();
	protected abstract String[] getTablePropertyNames();
	protected abstract String[] getTablePropertyHeaders();
	
	protected AbstractTableEditorView(final Class<T> beanType, final PagingAndSortingRepository<T, String> repository) {
		this.beanType = beanType;
		this.repository = repository;
		
		build();
    	
		this.fieldGroup = BeanBinder.bind(buildEmpty(), this, true);
	}
	
	protected Component buildContent() {

		VerticalLayout root = new VerticalLayout();
        root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.setMargin(false);
        layout.setSizeFull();
        root.addComponent(layout);
        root.setComponentAlignment(layout, Alignment.TOP_LEFT);
        
        this.table = buildTable(); 
        this.table.setWidth(100f, Unit.PERCENTAGE);
        layout.addComponent(this.table);
        layout.setExpandRatio(this.table, 7f);
        
        VerticalLayout formLayout = new VerticalLayout();
        formLayout.setWidth(100f, Unit.PERCENTAGE);
        formLayout.setStyleName(Style.Rating.PANEL);
        formLayout.setSpacing(false);
        formLayout.setMargin(false);
        formLayout.setSizeFull();
        
        formLayout.addComponent(buildForm());
        formLayout.addComponent(buttonLayout);
            
        layout.addComponent(formLayout);
        layout.setExpandRatio(formLayout, 5f);

        return root;
    }
	
	private MTable<T> buildTable() {
		
		SpringEntityProvider<T> entityProvider = new SpringEntityProvider<T>(repository);
		
		MTable<T> table = new MTable<T>(beanType)
				.setBeans(new SortableLazyList<T>(entityProvider, entityProvider, 100))
                .withProperties(getTablePropertyNames())
                .withColumnHeaders(getTablePropertyHeaders());
        
		table.addMValueChangeListener(event -> {
			saveButton.setEnabled(true);
            	fieldGroup.setItemDataSource(event.getValue());
            });
		
		return table;
	}

	protected BeanFieldGroup<T> getFieldGroup() { return this.fieldGroup; }
	protected void setRepository(final PagingAndSortingRepository<T, String> repository) { this.repository = repository; }
	protected PagingAndSortingRepository<T, String> getRepository() { return this.repository; }
	protected MTable<T> getTable() { return this.table; }
}
