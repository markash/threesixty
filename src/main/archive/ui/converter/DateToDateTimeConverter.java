package za.co.yellowfire.threesixty.ui.converter;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.DateTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A converter that converts from {@link DateTime} to {@link Date} and back. 
 * 
 * @author Mark P Ashworth
 * @since 0.0.1
 */
public class DateToDateTimeConverter implements Converter<Date, DateTime> {
	private static final long serialVersionUID = 1L;
	
	public DateToDateTimeConverter() { }

	@Override
    public DateTime convertToModel(
    		Date value, 
    		Class<? extends DateTime> targetType,
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {

		return value != null ? DateTime.parse(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(value)) : null;
    }

    @Override
    public Date convertToPresentation(
    		DateTime value,
            Class<? extends Date> targetType, 
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
    	try {
    		return value != null ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(value.toString()) : null;
    	} catch (ParseException e) {
    		throw new ConversionException("Unable to convert " + value + " to Date.", e);
    	}
    }
    
    @Override
    public Class<DateTime> getModelType() { return DateTime.class; }
    @Override
    public Class<Date> getPresentationType() { return Date.class; }
}