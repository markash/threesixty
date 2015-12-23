package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

public class LabelBuilder {

	public static Label blank() {
		return new Label("");
	}
	
	public static Label build(final String content) {
		return new Label(content);
	}
	
	public static Label build(final String content, final String...styles) {
		Label label =  new Label(content);
		
		if (styles != null) {
			for(String style : styles) {
				label.setStyleName(style);
			}
		}
		
		return label;
	}
	
	public static Label build(final String content, final ContentMode contentMode) {
		return new Label(content, contentMode);
	}
	
	public static Label build(final String content, final ContentMode contentMode, final String...styles) {
		Label label = new Label(content, contentMode);
		
		if (styles != null) {
			for(String style : styles) {
				label.setStyleName(style);
			}
		}
		return label;
	}
}
