package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.data.Binder;
import com.vaadin.data.HasValue;
import com.vaadin.server.Responsive;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.v7.data.Property;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitHandler;
import org.springframework.data.domain.Persistable;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractEntityEditForm<T extends Persistable<String>> extends HorizontalLayout {
	
	public static String[] DEFAULT_NESTED_PROPERTIES = new String[] {};

    protected TextField idField = new TextField("Id");
	private Binder<T> binder;
	//private LinkedHashSet<DirtyListener> dirtyListeners = new LinkedHashSet<>();
	
	private boolean layoutCompleted = false;
	
	public AbstractEntityEditForm(Class<T> entityClass) {
		setSpacing(true);
		setMargin(false);
		setSizeFull();
		Responsive.makeResponsive(this);

		this.binder = new Binder<>(entityClass);
		this.binder.bind(this.idField, "id");
        this.idField.setWidth(100.0f, Unit.PERCENTAGE);

        /*TODO Add back null representation*/
        //idField.setNullRepresentation("");
	}

	/**
	 * Returns the list of nested properties that the form group should bind to. The default
	 * is an empty array.
	 * @return An array of nested properties in Java object notation
	 */
	protected String[] getNestedProperties() { return DEFAULT_NESTED_PROPERTIES; }

    public Binder<T> getBinder() {
        return binder;
    }

    /**
     * Get the
     * @return The value of the form
     */
	public T getValue() { 
		return this.binder != null ? this.binder.getBean() : null;
	}
	
	void bindToEmpty() {
		bind(null);
	}
	
	public void bind(final T newValue) {
		T value = newValue != null ? buildEntity(newValue) :buildEntity(buildEmpty());
		this.binder.setBean(value);
		
		//updateDependentFields();
		//updateFieldConstraints();
		//registerDirtyListener();
	}

    /**
     * @deprecated
     */
	public void discard() {
		//this.binder.discard();
	}

    /**
     * @deprecated
     */
	public void commit() throws CommitException {
		//this.fieldGroup.commit();
	}
	
	protected boolean isModified() {
		return this.binder.hasChanges();
	}

    protected boolean isValid() {
		return this.binder.isValid();
	}

    /**
     * @deprecated
     */
	public void addCommitHandler(final CommitHandler commitHandler) {
		//if (commitHandler != null) {
		//	this.fieldGroup.addCommitHandler(commitHandler);
		//}
	}
	
	void addDirtyListener(final DirtyListener listener) {
//		if (listener != null) {
//			this.dirtyListeners.add(listener);
//		}
	}
	
	public void removeDirtyListener(final DirtyListener listener) {
//		if (listener != null) {
//			this.dirtyListeners.remove(listener);
//		}
	}
	
	private void registerDirtyListener() {
		/* Link the changing of the id text with a form dirty since this
		 * is the only field on the form sometimes which makes it difficult
		 * for the user to know that a tab is required to enable the Save button
		 */
		if (!idField.isReadOnly()) {
			idField.addValueChangeListener(this::onTextChange);
		}
		
		/*
		 * Link a value change to all other fields on the form
		 */
//		for(Field<?> field : this.fieldGroup.getFields()) {
//			field.removeValueChangeListener(this::onValueChange);
//			field.addValueChangeListener(this::onValueChange);
//		}
	}
	
	/**
	 * Provide a hoot for subclasses to update dependant fields
	 */
	private void updateDependentFields() { }
	
	/**
	 * Update the field constraints to the new bound value
	 */
//	protected void updateFieldConstraints() {
//		idField.setEnabled(getValue().isNew());
//
//		for(Field<?> field : fieldGroup.getFields()) {
//			field.removeValueChangeListener(this::onValueChange);
//			field.addValueChangeListener(this::onValueChange);
//		}
//	}
		
	protected abstract T buildEmpty();
	
	/**
	 * Provides the form the capability of enriching the entity with data that is not part of the entity read from
	 * the data source.
	 * 
	 * @param entity The entity
	 * @return The enriched entity
	 */
	protected T buildEntity(T entity) {
		return entity;
	}
	
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
	
	protected void onValueChange(final HasValue.ValueChangeEvent event) {
//		if (isModified()) {
//			fireFormDirty(new FormDirtyEvent(event));
//		}
	}
	
	private void onTextChange(final HasValue.ValueChangeEvent<String> event) {
		//fireFormDirty(new FormDirtyEvent());
	}
		
//	protected void fireFormClean() {
//		fireFormDirty(new FormDirtyEvent(DirtyStatus.CLEAN));
//	}
	
//	private void fireFormDirty(final FormDirtyEvent event) {
//		if (dirtyListeners != null) {
//			for (DirtyListener listener : dirtyListeners) {
//				listener.onDirty(event);
//			}
//		}
//	}

	/**
	 * A listener to determine if the form is dirty (i.e. a field has changed) or 
	 * clean (i.e. the fields are persisted). The dirty / clean state of the form usual determines
	 * whether functionality like Save, Reset, Delete are enabled.
     */
    public interface DirtyListener extends Serializable {
        void onDirty(DirtyEvent event);
    }
    
    public enum DirtyStatus {
    	DIRTY,
    	CLEAN
    }
    
    public interface DirtyEvent extends Serializable {
        Property<?> getProperty();
        DirtyStatus getStatus();
        boolean isRecalculationRequired();
    }
    
//    public static class FormDirtyEvent<T> implements DirtyEvent {
//    	private final Property<?> property;
//    	private final DirtyStatus status;
//    	private final boolean recalculationRequired;
//
//    	FormDirtyEvent() {
//    		this(null, false);
//    	}
//    	FormDirtyEvent(final DirtyStatus status) {
//    		this(null, false, status);
//    	}
//    	FormDirtyEvent(HasValue.ValueChangeEvent<?> property) {
//    		this(property, false);
//    	}
//    	public FormDirtyEvent(final HasValue.ValueChangeEvent<?> event, final boolean recalculationRequired) {
//    		this(event, recalculationRequired, DirtyStatus.DIRTY);
//    	}
//    	FormDirtyEvent(final HasValue.ValueChangeEvent<?> event, final boolean recalculationRequired, final DirtyStatus status) {
//    		this.event = event;
//    		this.status = status;
//    		this.recalculationRequired = recalculationRequired;
//    	}
//
//    	public Event<?> getProperty() { return this.property; }
//    	public DirtyStatus getStatus() { return this.status; }
//    	public boolean isRecalculationRequired() { return this.recalculationRequired; }
//    }
}
