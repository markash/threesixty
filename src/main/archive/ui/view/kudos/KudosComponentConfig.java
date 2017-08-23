package za.co.yellowfire.threesixty.ui.view.kudos;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.Ideal;
import za.co.yellowfire.threesixty.domain.kudos.IdealRepository;
import za.co.yellowfire.threesixty.ui.I8n;

@Configuration
public class KudosComponentConfig {

	@Bean @Qualifier("kudos")
	public ComboBox<Ideal> idealField(final IdealRepository repository) {
		ComboBox<Ideal> field = new ComboBox<>("Value", repository.findByActive(true));
		field.setWidth(100.0f, Unit.PERCENTAGE);
		return field;
	}
	
	@Bean @Qualifier("kudos")
	public ComboBox<Badge> badgeField(final BadgeRepository repository) {
		ComboBox<Badge> field = new ComboBox<>(I8n.Kudos.Fields.BADGE, repository.findByActive(true));
		field.setWidth(100.0f, Unit.PERCENTAGE);
		return field;
	}
	
	@Bean @Qualifier("kudos")
	public ComboBox recipientField() {
		ComboBox field = new ComboBox(I8n.Kudos.Fields.RECIPIENT);
		field.setWidth(100.0f, Unit.PERCENTAGE);
		field.setEnabled(false);
		return field;
	}
	
	@Bean @Qualifier("kudos")
	public RichTextArea messageField() {
		RichTextArea field = new RichTextArea(I8n.Kudos.Fields.MESSAGE);
		field.setWidth(100.0f, Unit.PERCENTAGE);
		return field;
	}
	
	@Bean @Qualifier("kudos")
	public TextArea motivationField() {
		TextArea field = new TextArea("Motivation");
		field.setWidth(100.0f, Unit.PERCENTAGE);
		field.setRows(7);
		return field;
	}
}
