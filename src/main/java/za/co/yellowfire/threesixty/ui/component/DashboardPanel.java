package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Notification;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.ui.DashboardPanelView;

@SuppressWarnings("serial")
public class DashboardPanel extends CssLayout {
	
	private DashboardPanelView view;
	
	public DashboardPanel(final Component wrapped, final DashboardPanelView view) {
		this.view = view;
		
		buildComponent(wrapped);
	}

	private void buildComponent(final Component wrapped) {
	  this.setWidth("100%");
	  this.addStyleName("dashboard-panel-slot");
	
	  CssLayout card = new CssLayout();
	  card.setWidth("100%");
	  card.addStyleName(ValoTheme.LAYOUT_CARD);
	
	  HorizontalLayout toolbar = new HorizontalLayout();
	  toolbar.addStyleName("dashboard-panel-toolbar");
	  toolbar.setWidth("100%");
	
	  Label caption = new Label(wrapped.getCaption());
	  caption.addStyleName(ValoTheme.LABEL_H4);
	  caption.addStyleName(ValoTheme.LABEL_COLORED);
	  caption.addStyleName(ValoTheme.LABEL_NO_MARGIN);
	  wrapped.setCaption(null);
	
	  MenuBar tools = new MenuBar();
	  tools.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
	  MenuItem max = tools.addItem("", FontAwesome.EXPAND, new Command() {
	
	      @Override
	      public void menuSelected(final MenuItem selectedItem) {
	          if (!getStyleName().contains("max")) {
	              selectedItem.setIcon(FontAwesome.COMPRESS);
	              view.toggleMaximized(DashboardPanel.this, true);
	          } else {
	              removeStyleName("max");
	              selectedItem.setIcon(FontAwesome.EXPAND);
	              view.toggleMaximized(DashboardPanel.this, false);
	          }
	      }
	  });
	  max.setStyleName("icon-only");
	  MenuItem root = tools.addItem("", FontAwesome.COG, null);
	  root.addItem("Configure", new Command() {
	      @Override
	      public void menuSelected(final MenuItem selectedItem) {
	          Notification.show("Not implemented in this demo");
	      }
	  });
	  root.addSeparator();
	  root.addItem("Close", new Command() {
	      @Override
	      public void menuSelected(final MenuItem selectedItem) {
	          Notification.show("Not implemented in this demo");
	      }
	  });
	
	  toolbar.addComponents(caption, tools);
	  toolbar.setExpandRatio(caption, 1);
	  toolbar.setComponentAlignment(caption, Alignment.MIDDLE_LEFT);
	
	  card.addComponents(toolbar, wrapped);
	  this.addComponent(card);
	}
}
