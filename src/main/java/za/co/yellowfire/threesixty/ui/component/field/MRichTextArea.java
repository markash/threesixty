package za.co.yellowfire.threesixty.ui.component.field;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.Resource;
import com.vaadin.ui.RichTextArea;

@SuppressWarnings("serial")
public class MRichTextArea extends RichTextArea {

	public MRichTextArea() {
		super();
		configureDefaults();
	}

	public MRichTextArea(Property<?> dataSource) {
		super(dataSource);
		configureDefaults();
	}

	public MRichTextArea(String caption, Property<?> dataSource) {
		super(caption, dataSource);
		configureDefaults();
	}

	public MRichTextArea(String caption, String value) {
		super(caption, value);
		configureDefaults();
	}

	public MRichTextArea(String caption) {
		super(caption);
		configureDefaults();
	}
	
	protected void configureDefaults() {
		setNullRepresentation("");
	}
	
	public MRichTextArea withConversionError(String message) {
        setConversionError(message);
        return this;
    }

    public MRichTextArea withConverter(Converter<String, ?> converter) {
        setConverter(converter);
        return this;
    }

    public MRichTextArea withFullWidth() {
        setWidth("100%");
        return this;
    }

    public MRichTextArea withReadOnly(boolean readOnly) {
        setReadOnly(readOnly);
        return this;
    }

    public MRichTextArea withValidator(Validator validator) {
        setImmediate(true);
        addValidator(validator);
        return this;
    }

    public MRichTextArea withWidth(float width, Unit unit) {
        setWidth(width, unit);
        return this;
    }

    public MRichTextArea withWidth(String width) {
        setWidth(width);
        return this;
    }

    public MRichTextArea withNullRepresentation(String nullRepresentation) {
        setNullRepresentation(nullRepresentation);
        return this;
    }

    public MRichTextArea withStyleName(String styleName) {
        setStyleName(styleName);
        return this;
    }

    public MRichTextArea withIcon(Resource icon) {
        setIcon(icon);
        return this;
    }
    
    public MRichTextArea withRequired(boolean required) {
        setRequired(required);
        return this;
    }
    
    public MRichTextArea withRequiredError(String requiredError) {
        setRequiredError(requiredError);
        return this;
    }
}
