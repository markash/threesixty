package za.co.yellowfire.threesixty.ui.converter;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;

/**
 * @author Mark P Ashworth
 * @since 0.0.1
 */
public class StringToAssessmentStatusConverter implements Converter<String, AssessmentStatus> {
	private static final long serialVersionUID = 1L;
	
	@Override
    public AssessmentStatus convertToModel(
    		String value, 
    		Class<? extends AssessmentStatus> targetType,
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
    	
        return AssessmentStatus.fromString(value);
    }

    @Override
    public String convertToPresentation(
    		AssessmentStatus value,
            Class<? extends String> targetType, 
            Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException {
        
    	return value.toString();
    }

    @Override
    public Class<AssessmentStatus> getModelType() { return AssessmentStatus.class; }
    @Override
    public Class<String> getPresentationType() { return String.class; }
}