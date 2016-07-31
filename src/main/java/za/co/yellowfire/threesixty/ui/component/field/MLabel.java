package za.co.yellowfire.threesixty.ui.component.field;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

@SuppressWarnings("serial")
public class MLabel extends Label {

	public MLabel() {
		super();
		configureDefaults();
	}

	@SuppressWarnings("rawtypes")
	public MLabel(Property contentSource) {
		super(contentSource);
		configureDefaults();
	}

	public MLabel(String caption) {
		super(caption);
		configureDefaults();
	}
	
	public MLabel(String content, ContentMode contentMode) {
		super(content, contentMode);
		configureDefaults();
	}
	
	@SuppressWarnings("rawtypes")
	public MLabel(Property contentSource, ContentMode contentMode) {
		super(contentSource, contentMode);
		configureDefaults();
	}
	
	protected void configureDefaults() {
		
	}

	public MLabel withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MLabel withWidth(String width) {
        setWidth(width);
        return this;
    }
    
    public MLabel withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }
    
    public MLabel valueChangeListener(final ValueChangeListener valueChangeListener) {
    	addValueChangeListener(valueChangeListener);
    	return this;
    }
}
