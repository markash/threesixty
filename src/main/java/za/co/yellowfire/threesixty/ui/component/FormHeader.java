package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.viritin.label.MLabel;

@SuppressWarnings("serial")
public class FormHeader extends CssLayout {

	public FormHeader(final String welcome, final String title) {
		addStyleName("labels");
		
		MLabel welcomeLabel = new MLabel(welcome)
        		.withUndefinedWidth()
        		.withStyleName(ValoTheme.LABEL_H4, ValoTheme.LABEL_COLORED);
        addComponent(welcomeLabel);

        MLabel titleLabel = new MLabel(title)
        		.withContentMode(ContentMode.HTML)
        		.withUndefinedWidth()
        		.withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_LIGHT);

        addComponent(titleLabel);
	}
}
