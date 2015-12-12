package za.co.yellowfire.threesixty.ui.view;

import org.springframework.data.domain.Persistable;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;

@SuppressWarnings("serial")
public abstract class AbstractEntityEditForm<T extends Persistable<String>> extends HorizontalLayout {
	
	@PropertyId("id")
    private TextField idField = new TextField("Id");
	
	private BeanFieldGroup<T> fieldGroup;
	
	public AbstractEntityEditForm() {
		setSpacing(true);
		setMargin(false);
		setSizeFull();
		
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        idField.setNullRepresentation("");
        
        addComponent(PanelBuilder.FORM(
        		idField));
        addComponent(new Label(""));
	}

	public T getValue() { return this.fieldGroup.getItemDataSource().getBean(); }
	
	public void bindToEmpty() {
		bind(null);
	}
	
	public void bind(final T newValue) {
		T value = newValue != null ? newValue : buildEmpty();
		this.fieldGroup = BeanBinder.bind(value, this, true);
		
		updateFieldContraints();
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
	
	protected void updateFieldContraints() {
		idField.setEnabled(getValue().isNew());
	}
	
	protected abstract T buildEmpty();
}
