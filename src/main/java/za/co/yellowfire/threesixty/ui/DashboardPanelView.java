package za.co.yellowfire.threesixty.ui;

import com.vaadin.navigator.View;
import com.vaadin.ui.Component;

public interface DashboardPanelView extends View {
	void toggleMaximized(final Component panel, final boolean maximized);
}
