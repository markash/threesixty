package za.co.yellowfire.threesixty.ui.view.user.notification;

//import java.util.Arrays;
//
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.vaadin.data.util.IndexedContainer;
//import com.vaadin.server.Sizeable.Unit;
//import com.vaadin.ui.ComboBox;
//import com.vaadin.ui.DateField;
//import com.vaadin.ui.TextArea;
//import com.vaadin.ui.TextField;
//
//import za.co.yellowfire.threesixty.domain.user.UserRepository;
//import za.co.yellowfire.threesixty.domain.user.notification.NotificationCategory;
//import za.co.yellowfire.threesixty.ui.I8n;

//@Configuration
public class NotificationComponentConfig {

//	@Bean @Qualifier("notification")
//	public ComboBox userField(final UserRepository repository) {
//		ComboBox field = new ComboBox(I8n.Notifications.Fields.USER, new IndexedContainer(repository.findByActive(true)));
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean @Qualifier("notification")
//	public ComboBox categoryField() {
//		ComboBox field = new ComboBox(I8n.Notifications.Fields.CATEGORY, new IndexedContainer(Arrays.asList(NotificationCategory.values())));
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setEnabled(false);
//		return field;
//	}	
//	
//	@Bean @Qualifier("notification")
//	public DateField timeField() {
//		DateField field = new DateField(I8n.Notifications.Fields.TIME);
//		field.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean  @Qualifier("notification")
//	public TextArea contentField() {
//		TextArea field = new TextArea(I8n.Notifications.Fields.CONTENT);
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setNullRepresentation("");
//		field.setEnabled(false);
//		return field;
//	}
//	
//	@Bean  @Qualifier("notification")
//	public TextField actionField() {
//		TextField field = new TextField(I8n.Notifications.Fields.ACTION);
//		field.setWidth(100.0f, Unit.PERCENTAGE);
//		field.setNullRepresentation("");
//		field.setEnabled(false);
//		return field;
//	}
}
