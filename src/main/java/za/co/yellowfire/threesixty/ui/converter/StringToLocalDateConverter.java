package za.co.yellowfire.threesixty.ui.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

/**
 * A converter that converts from {@link LocalDate} to {@link String} and back. 
 * <p>
 * Leading and trailing white spaces are ignored when converting from a String.
 * </p>
 * <p>
 * Override and overwrite {@link #getFormat(Locale)} to use a different format.
 * </p>
 * 
 * @author Mark P Ashworth
 * @since 0.0.1
 */
public class StringToLocalDateConverter implements Converter<String, LocalDate> {
	private static final long serialVersionUID = 1L;
	
	private DateTimeFormatter formatter;
	
	public StringToLocalDateConverter() {
		this(DateTimeFormatter.ISO_DATE);
	}

    public StringToLocalDateConverter(DateTimeFormatter formatter) {
		super();
		this.formatter = formatter;
	}

	@Override
    public LocalDate convertToModel(
    		String value, 
    		Class<? extends LocalDate> targetType,
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
    	
        if (targetType != getModelType()) {
            throw new ConversionException("Converter only supports " + 
            		getModelType().getName() + 
            		" (targetType was " + 
            		targetType.getName() + ")");
        }

        if (value == null) {
            return null;
        }

        // Remove leading and trailing white space
        value = value.trim();

        return LocalDate.parse(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.vaadin.data.util.converter.Converter#convertToPresentation(java.lang
     * .Object, java.lang.Class, java.util.Locale)
     */
    @Override
    public String convertToPresentation(
    		LocalDate value,
            Class<? extends String> targetType, 
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
        
    	if (value == null) {
            return null;
        }

        return value.format(formatter);
    }


    @Override
    public Class<LocalDate> getModelType() { return LocalDate.class; }
    @Override
    public Class<String> getPresentationType() { return String.class; }
}