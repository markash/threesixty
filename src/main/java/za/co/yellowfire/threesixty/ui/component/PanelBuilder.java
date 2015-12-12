package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.ui.Style;

public class PanelBuilder {

	public static Layout FORM(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(Style.Rating.FIELDS);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	public static Layout VERTICAL(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(Style.Rating.FIELDS);
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	public static Layout HORIZONTAL(final Component...components) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
}
