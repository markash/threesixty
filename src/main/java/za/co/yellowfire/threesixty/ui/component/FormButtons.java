package za.co.yellowfire.threesixty.ui.component;

import java.util.HashMap;
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;

import za.co.yellowfire.threesixty.ui.Style;

public class FormButtons extends HorizontalLayout {
	private static final long serialVersionUID = 1L;
	
	private Map<String, Button> buttons = new HashMap<>();
	
	public FormButtons() {
		super();
		
		this.setStyleName(Style.Rating.BUTTONS);
		this.setWidth(100.0f, Unit.PERCENTAGE);
		this.setMargin(false);
		this.setSpacing(true);
	}
	
	public FormButtons(final Button...buttons) {
		this();
		
		for(Button button : buttons) {
			addButton(button);
		}
	}
	
	public void addButton(final String caption, final ClickListener listener) {
		addButton(ButtonBuilder.build(caption, listener));
	}
	
	public void addButton(final String caption,  final String styleName, final ClickListener listener) {
		addButton(ButtonBuilder.build(caption, styleName, listener));
	}
	
	public void addButton(@NotNull final Button button) {
		this.buttons.put(button.getCaption(), button);
		addComponent(button);
	}
}
