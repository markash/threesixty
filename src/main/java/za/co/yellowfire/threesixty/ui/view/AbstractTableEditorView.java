package za.co.yellowfire.threesixty.ui.view;

import java.io.Serializable;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.MTable;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;

/**
 * Abstract table editor view
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public abstract class AbstractTableEditorView<T, ID extends Serializable> extends AbstractDashboardPanel {
	private static final long serialVersionUID = 1L;

	protected static final String BUTTON_OK = "Save";
	protected static final String BUTTON_RESET = "Reset";
	protected static final String BUTTON_DELETE = "Delete";
	protected static final String BUTTON_ADD = "New";
	
    private Button ok;
    private Button reset;
    private Button add;
    private Button delete;
    private HorizontalLayout buttonLayout;
    
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
        formLayout.addComponent(buildButtons());
            
        layout.addComponent(formLayout);
        layout.setExpandRatio(formLayout, 5f);

        return root;
    }
	
	private MTable<T> buildTable() {
		
		SpringEntityProvider<T> entityProvider = new SpringEntityProvider<T>(repository);
		
		@SuppressWarnings("unchecked")
		MTable<T> table = new MTable<T>(beanType)
				.setBeans(new SortableLazyList<T>(entityProvider, entityProvider, 100))
                .withProperties(getTablePropertyNames())
                .withColumnHeaders(getTablePropertyHeaders());
        
		table.addMValueChangeListener(event -> {
            	ok.setEnabled(true);
            	fieldGroup.setItemDataSource(event.getValue());
            });
		
		return table;
	}

	//TODO Replace with FormButtons
	protected Component buildButtons() {
        buttonLayout = new HorizontalLayout();
        buttonLayout.setStyleName(Style.Rating.BUTTONS);
        buttonLayout.setWidth(100.0f, Unit.PERCENTAGE);
        buttonLayout.setMargin(false);
        buttonLayout.setSpacing(true);
        
        this.ok = buildOKButton();
        this.ok.focus();
        buttonLayout.addComponent(this.ok);
        
        this.add = buildAddButton();
        buttonLayout.addComponent(this.add);
        
        this.delete = buildDeleteButton();
        buttonLayout.addComponent(this.delete);
        
        this.reset = buildResetButton();
        buttonLayout.addComponent(this.reset);
        return buttonLayout;
    }
	
	protected Button buildOKButton() {
		Button button = new Button("Save");
        button.addStyleName(ValoTheme.BUTTON_PRIMARY);
        button.setWidth(100.0f, Unit.PERCENTAGE);
        button.addClickListener(e -> { save(e); });
        return button;
	}
	
	protected Button buildResetButton() {
		Button button = new Button("Reset");
		button.setWidth(100.0f, Unit.PERCENTAGE);
        button.addClickListener(e -> { reset(e); });
        return button;
	}
	
	protected Button buildAddButton() {
		Button button = new Button(BUTTON_ADD);
		button.setWidth(100.0f, Unit.PERCENTAGE);
        button.addClickListener( e -> { add(e); });
        return button;
	}
	
	protected Button buildDeleteButton() {
		Button button = new Button("Delete");
		button.setWidth(100.0f, Unit.PERCENTAGE);
        button.addClickListener(e -> { delete(e); });
        return button;
	}

	protected BeanFieldGroup<T> getFieldGroup() { return this.fieldGroup; }
	protected void setRepository(final PagingAndSortingRepository<T, String> repository) { this.repository = repository; }
	protected PagingAndSortingRepository<T, String> getRepository() { return this.repository; }
	protected MTable<T> getTable() { return this.table; }
}
