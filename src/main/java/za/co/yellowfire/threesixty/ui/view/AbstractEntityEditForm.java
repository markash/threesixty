package za.co.yellowfire.threesixty.ui.view;

import java.io.Serializable;
import java.util.LinkedHashSet;

import org.springframework.data.domain.Persistable;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
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
		for(Field<?> field : this.fieldGroup.getFields()) {
			field.removeValueChangeListener(this::onValueChange);
			field.addValueChangeListener(this::onValueChange);
		}
	}
	
	protected void updateFieldContraints() {
		idField.setEnabled(getValue().isNew());
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
			for (DirtyListener listener : dirtyListeners) {
				listener.onDirty(new FormDirtyEvent(event.getProperty()));
			}
		}
	}
	
	/**
	 * 
     */
    public interface DirtyListener extends Serializable {
        public void onDirty(DirtyEvent event);
    }
    
    public interface DirtyEvent extends Serializable {
        public Property<?> getProperty();
    }
    
    public static class FormDirtyEvent implements DirtyEvent {
    	private final Property<?> property;
    	public FormDirtyEvent(Property<?> property) {
    		this.property = property;
    	}
    	public Property<?> getProperty() { return this.property; }
    }
}
