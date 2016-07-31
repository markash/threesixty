package za.co.yellowfire.threesixty.ui.component;

import org.vaadin.viritin.label.MLabel;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class FormHeader extends CssLayout {

	public FormHeader(final String welcome, final String title) {
		addStyleName("labels");
		
		MLabel welcomeLabel = new MLabel(welcome)
        		.withWidthUndefined()
        		.withStyleName(ValoTheme.LABEL_H4, ValoTheme.LABEL_COLORED);
        addComponent(welcomeLabel);

        MLabel titleLabel = new MLabel(title)
        		.withContentMode(ContentMode.HTML)
        		.withWidthUndefined()
        		.withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_LIGHT);

        addComponent(titleLabel);
	}
}
