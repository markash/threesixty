package za.co.yellowfire.threesixty.ui.component;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;

public class ButtonBuilder {

	public static Button build(final String caption, final ClickListener listener) {
		return build(caption, (String)null, listener);
	}
	
	public static Button build(final String caption, final FontAwesome fontIcon, final ClickListener listener) {
		Button button = new Button(caption);
		button.setIcon(fontIcon);
		button.setWidth(100.0f, Unit.PERCENTAGE);
        button.addClickListener(listener);
        return button;
	}
	
	public static Button build(final String caption, final String styleName, final ClickListener listener) {
		Button button = new Button(caption);
		if (StringUtils.isNotBlank(styleName)) {
			button.setStyleName(styleName);
		}
		button.setWidth(100.0f, Unit.PERCENTAGE);
        button.addClickListener(listener);
        return button;
	}
}
