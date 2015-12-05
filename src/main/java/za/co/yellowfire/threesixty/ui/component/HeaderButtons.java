package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class HeaderButtons extends HorizontalLayout {
	private static final long serialVersionUID = 1L;
	
	public HeaderButtons(final Component component, final Component...components) {
		this(combine(component, components));
	}
	
	public HeaderButtons(final Component...components) {
		for (Component button : components) {
			addComponent(button);
		}
		
		setSpacing(true);
        addStyleName("toolbar");
	}
	
	private static Component[] combine(final Component component, final Component...components) {
		Component[] result = new Component[components.length + 1];
		
		result[0] = component;
		for(int i = 0; i < components.length; i++) {
			result[i+1] = components[i];
		}
		return result;
	}
}
