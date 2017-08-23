package za.co.yellowfire.threesixty.ui.converter;

import com.vaadin.data.util.converter.Converter;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class DefaultConverterFactory extends com.vaadin.data.util.converter.DefaultConverterFactory {
	private static final long serialVersionUID = 1L;

	protected Converter<Date, ?> createDateConverter(Class<?> sourceType) {
		if (LocalDate.class.isAssignableFrom(sourceType)) {
			return new DateToLocalDateConverter();
		} else if (DateTime.class.isAssignableFrom(sourceType)) {
			return new DateToDateTimeConverter();
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
