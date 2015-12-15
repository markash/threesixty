package za.co.yellowfire.threesixty.ui.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.TextField;

@Configuration
public class ComponentConfiguration {
	
	@Bean
	public TextField descriptionField() {
		TextField field = new TextField("Description");
		field.setWidth(100.0f, Unit.PERCENTAGE);
		field.setNullRepresentation("");
		return field;
	}
}
