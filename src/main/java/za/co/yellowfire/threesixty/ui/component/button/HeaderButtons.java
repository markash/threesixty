package za.co.yellowfire.threesixty.ui.component.button;

import java.util.Arrays;
import java.util.Collection;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;

public class HeaderButtons extends HorizontalLayout {
	private static final long serialVersionUID = 1L;
			
	public HeaderButtons(final Component...components) {
		this(Arrays.asList(components));
	}
	
	public HeaderButtons(final Collection<Component> components) {
		for (Component button : components) {
			addComponent(button);
		}
		
		setSpacing(true);
        addStyleName("toolbar");
	}
	
	public static Component[] combine(final Component component, final Component...components) {
		Component[] result = new Component[components != null ? components.length + 1 : 1];
		
		result[0] = component;
		if (components != null) {
			for(int i = 0; i < components.length; i++) {
				result[i+1] = components[i];
			}
		}
		return result;
	}
}
