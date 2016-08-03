package za.co.yellowfire.threesixty.ui.view;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;

import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.ui.I8n;

//@Configuration
public class ComponentConfiguration {
	
//	@Bean
//	public TextField idField() {
//		TextField field = new TextField(I8n.Fields.ID);
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setNullRepresentation("");
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean
//	public ComboBox createdByField(final UserRepository repository) {
//		ComboBox field = new ComboBox(I8n.Fields.CREATED_BY, new IndexedContainer(repository.findAll()));
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean
//	public ComboBox modifiedByField(final UserRepository repository) {
//		ComboBox field = new ComboBox(I8n.Fields.MODIFIED_BY, new IndexedContainer(repository.findAll()));
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean
//	public DateField createdAtField() {
//		DateField field = new DateField(I8n.Fields.CREATED_TIME);
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean
//	public DateField modifiedAtField() {
//		DateField field = new DateField(I8n.Fields.MODIFIED_BY);
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean
//	public TextField descriptionField() {
//		TextField field = new TextField("Description");
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setNullRepresentation("");
//		return field;
//	}
}
