package za.co.yellowfire.threesixty.ui.view;

import java.io.Serializable;
import java.util.LinkedHashSet;

import org.springframework.data.domain.Persistable;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;

@SuppressWarnings("serial")
public abstract class AbstractEntityEditForm<T extends Persistable<String>> extends HorizontalLayout {
	
	@PropertyId("id")
    protected TextField idField = new TextField("Id");
	
	private BeanFieldGroup<T> fieldGroup;
	private LinkedHashSet<DirtyListener> dirtyListeners = new LinkedHashSet<>();
	
	private boolean layoutCompleted = false;
	
	public AbstractEntityEditForm() {
		setSpacing(true);
		setMargin(false);
		setSizeFull();
		Responsive.makeResponsive(this);
		
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        idField.setNullRepresentation("");
	}

	public T getValue() { return this.fieldGroup.getItemDataSource().getBean(); }
	
	public void bindToEmpty() {
		bind(null);
	}
	
	public void bind(final T newValue) {
		T value = newValue != null ? newValue : buildEmpty();
		this.fieldGroup = BeanBinder.bind(value, this, true);
				
		updateFieldContraints();
		registerDirtyListener();
	}
	
	public void discard() {
		this.fieldGroup.discard();
	}
	
	public void commit() throws CommitException {
		this.fieldGroup.commit();
	}
	
	public boolean isModified() {
		return this.fieldGroup.isModified();
	}
	
	public boolean isValid() {
		return this.fieldGroup.isValid();
	}
	
	public void addDirtyListener(final DirtyListener listener) {
		if (listener != null) {
			this.dirtyListeners.add(listener);
		}
	}
	
	public void removeDirtyListener(final DirtyListener listener) {
		if (listener != null) {
			this.dirtyListeners.remove(listener);
		}
	}
	
	protected void registerDirtyListener() {
		/* Link the changing of the id text with a form dirty since this
		 * is the only field on the form sometimes which makes it difficult
		 * for the user to know that a tab is required to enable the Save button
		 */
		if (!idField.isReadOnly()) {
			idField.addTextChangeListener(this::onTextChange);
		}
		
		/*
		 * Link a value change to all other fields on the form
		 */
		for(Field<?> field : this.fieldGroup.getFields()) {
			field.removeValueChangeListener(this::onValueChange);
			field.addValueChangeListener(this::onValueChange);
		}
	}
	
	protected void updateFieldContraints() {
		idField.setEnabled(getValue().isNew());
		
		for(Field<?> field : fieldGroup.getFields()) {
			field.removeValueChangeListener(this::onValueChange);
			field.addValueChangeListener(this::onValueChange);
		}
	}
	
	protected abstract T buildEmpty();
	
	public void layout() {
		if (!layoutCompleted) {
			internalLayout();
			layoutCompleted = true;
		}
	}
	
	protected void internalLayout() {
		addComponent(PanelBuilder.FORM(
        		idField));
        addComponent(new Label(""));
	}
	
	protected void onValueChange(final ValueChangeEvent event) {
		if (isModified()) {
			fireFormDirty(new FormDirtyEvent(event.getProperty()));
		}
	}
	
	protected void onTextChange(final TextChangeEvent event) {
		fireFormDirty(new FormDirtyEvent());
	}
		
	protected void fireFormClean() {
		fireFormDirty(new FormDirtyEvent(DirtyStatus.CLEAN));
	}
	
	protected void fireFormDirty(final FormDirtyEvent event) {
		if (dirtyListeners != null) {
			for (DirtyListener listener : dirtyListeners) {
				listener.onDirty(event);
			}
		}
	}

	/**
	 * A listener to determine if the form is dirty (i.e. a field has changed) or 
	 * clean (i.e. the fields are persisted). The dirty / clean state of the form usual determines
	 * whether functionality like Save, Reset, Delete are enabled.
     */
    public interface DirtyListener extends Serializable {
        public void onDirty(DirtyEvent event);
    }
    
    public static enum DirtyStatus {
    	DIRTY,
    	CLEAN
    }
    
    public interface DirtyEvent extends Serializable {
        public Property<?> getProperty();
        public DirtyStatus getStatus(); 
        public boolean isRecalculationRequired();
    }
    
    public static class FormDirtyEvent implements DirtyEvent {
    	private final Property<?> property;
    	private final DirtyStatus status;
    	private final boolean recalculationRequired;
    	
    	public FormDirtyEvent() {
    		this(null, false);
    	}
    	public FormDirtyEvent(final DirtyStatus status) {
    		this(null, false, status);
    	}
    	public FormDirtyEvent(Property<?> property) {
    		this(property, false);
    	}
    	public FormDirtyEvent(final Property<?> property, final boolean recalculationRequired) {
    		this(property, recalculationRequired, DirtyStatus.DIRTY);
    	}
    	public FormDirtyEvent(final Property<?> property, final boolean recalculationRequired, final DirtyStatus status) {
    		this.property = property;
    		this.status = status;
    		this.recalculationRequired = recalculationRequired;
    	}
    	
    	public Property<?> getProperty() { return this.property; }
    	public DirtyStatus getStatus() { return this.status; }
    	public boolean isRecalculationRequired() { return this.recalculationRequired; }
    }
}
