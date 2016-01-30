package za.co.yellowfire.threesixty.ui.component.field;

import java.util.Collection;

import com.vaadin.data.Container;
import com.vaadin.ui.ComboBox;

import za.co.yellowfire.threesixty.ui.component.container.RepositoryContainer;

@SuppressWarnings("serial")
public class MComboBox extends ComboBox {

	public MComboBox() {
		super();
		configureDefaults();
	}

	public MComboBox(String caption, Collection<?> options) {
		super(caption, options);
		configureDefaults();
	}

	public MComboBox(String caption, Container dataSource) {
		super(caption, dataSource);
		configureDefaults();
	}

	public MComboBox(Container dataSource) {
		super("", dataSource);
		configureDefaults();
	}
	
	public MComboBox(String caption) {
		super(caption);
		configureDefaults();
	}
	
	
	protected void configureDefaults() {
		
	}

	public void refresh() {
		Container c = getContainerDataSource();
		if (c instanceof RepositoryContainer) {
			((RepositoryContainer<?>) c).refresh();
		}
	}
	
	public MComboBox withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MComboBox withWidth(String width) {
        setWidth(width);
        return this;
    }
    
    public MComboBox withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }
    
    public MComboBox withEnabled(boolean enabled) {
    	setEnabled(enabled);
    	return this;
    }
    
    public MComboBox withDisabled() {
    	return withEnabled(false);
    }
    
    public MComboBox valueChangeListener(final ValueChangeListener valueChangeListener) {
    	addValueChangeListener(valueChangeListener);
    	return this;
    }
}
