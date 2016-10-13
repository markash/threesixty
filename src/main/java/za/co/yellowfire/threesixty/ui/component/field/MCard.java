package za.co.yellowfire.threesixty.ui.component.field;

import org.vaadin.viritin.button.MButton;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.domain.statistics.CounterStatistic;

public class MCard extends CustomComponent {
	private static final long serialVersionUID = 1L;

	private String title;
	private FontAwesome icon;
	private String description;
	private String viewName;
	private CounterStatistic statistic;
	
	public MCard(final String title, final FontAwesome icon, final String description, final CounterStatistic statistic, final String viewName) {
		super();
		
		this.title = title;
		this.icon = icon;
		this.description = description;
		this.statistic = statistic;
		this.viewName = viewName;
		
		setPrimaryStyleName("valo-card"); 
		setCompositionRoot(buildContent());
		setSizeUndefined();
	}
	
	protected CssLayout buildContent() {
		final CssLayout content = new CssLayout();
		
		final CssLayout container = new CssLayout();
		container.setPrimaryStyleName("valo-card-container");
		
		Label image = new Label(icon.getHtml(), ContentMode.HTML);
        Label text = new Label("&nbsp;&nbsp;<strong>" + this.statistic.getFormattedValue() + " </strong> " + this.title, ContentMode.HTML);
        HorizontalLayout title = new HorizontalLayout(image, text);
        title.setPrimaryStyleName("valo-card-title");
        
        Label description = new Label(this.description);
        description.setPrimaryStyleName("valo-card-description");
        
        final CssLayout footer = new CssLayout();
		footer.setPrimaryStyleName("valo-card-footer");
		
        MButton button = new MButton(FontAwesome.ARROW_CIRCLE_O_RIGHT);
        button.setPrimaryStyleName("valo-card-link");
        button.addClickListener(event ->  UI.getCurrent().getNavigator().navigateTo(viewName));
        
        container.addComponent(title);
        container.addComponent(description);
        footer.addComponent(button);
        
        content.addComponent(container);
        content.addComponent(footer);
		return content;
	}
}
