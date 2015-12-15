package za.co.yellowfire.threesixty.ui.view.kudos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextArea;

import za.co.yellowfire.threesixty.domain.kudos.IdealRepository;

@Configuration
public class KudosComponentConfig {

	@Bean
	public ComboBox idealField(final IdealRepository repository) {
		ComboBox field = new ComboBox("Value", new IndexedContainer(repository.findByActive(true)));
		field.setWidth(100.0f, Unit.PERCENTAGE);
		return field;
	}
	
	@Bean
	public TextArea motivationField() {
		TextArea field = new TextArea("Motivation");
		field.setWidth(100.0f, Unit.PERCENTAGE);
		field.setNullRepresentation("");
		return field;
	}
}
