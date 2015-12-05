package za.co.yellowfire.threesixty.ui.converter;

import java.time.LocalDate;
import java.util.Date;

import com.vaadin.data.util.converter.Converter;

public class DefaultConverterFactory extends com.vaadin.data.util.converter.DefaultConverterFactory {
	private static final long serialVersionUID = 1L;

	protected Converter<Date, ?> createDateConverter(Class<?> sourceType) {
		if (LocalDate.class.isAssignableFrom(sourceType)) {
			return new DateToLocalDateConverter();
		}
		
		return super.createDateConverter(sourceType);
	}
	
	protected Converter<String, ?> createStringConverter(Class<?> sourceType) {
		if (LocalDate.class.isAssignableFrom(sourceType)) {
			return new StringToLocalDateConverter();
		}
		
		return super.createStringConverter(sourceType);
	}
}
