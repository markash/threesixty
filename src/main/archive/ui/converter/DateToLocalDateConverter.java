package za.co.yellowfire.threesixty.ui.converter;

import com.vaadin.data.util.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

/**
 * A converter that converts from {@link LocalDate} to {@link Date} and back. 
 * 
 * @author Mark P Ashworth
 * @since 0.0.1
 */
public class DateToLocalDateConverter implements Converter<Date, LocalDate> {
	private static final long serialVersionUID = 1L;
	
	public DateToLocalDateConverter() { }

	@Override
    public LocalDate convertToModel(
    		Date value, 
    		Class<? extends LocalDate> targetType,
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {

		return value != null ? LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(value)) : null;
    }

    @Override
    public Date convertToPresentation(
    		LocalDate value,
            Class<? extends Date> targetType, 
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
    	try {
    		return value != null ? new SimpleDateFormat("yyyy-MM-dd").parse(value.toString()) : null;
    	} catch (ParseException e) {
    		throw new ConversionException("Unable to convert " + value + " to Date.", e);
    	}
    }
    
    @Override
    public Class<LocalDate> getModelType() { return LocalDate.class; }
    @Override
    public Class<Date> getPresentationType() { return Date.class; }
}