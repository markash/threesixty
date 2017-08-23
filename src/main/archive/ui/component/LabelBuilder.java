package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;

import java.util.ArrayList;
import java.util.List;

public class LabelBuilder {

	public static Label blank() {
		return new Label("");
	}
	
	public static Label build(final String content) {
		return new Label(content);
	}
	
	public static Label build(final String content, final List<String> styles) {
		return build(content, styles.toArray(new String[styles.size()]));
	}

	public static Label build(final String content, final String primaryStyle, final List<String> additionalStyles) {
		List<String> styles = new ArrayList<>();
		
		if(primaryStyle != null) {
			styles.add(primaryStyle);
		}
		if (additionalStyles != null) {
			styles.addAll(additionalStyles);
		}
		return build(content, styles.toArray(new String[styles.size()]));
	}
	
	public static Label build(final String content, final String...styles) {
		Label label =  new Label(content);
		
		if (styles != null) {
			for(String style : styles) {
				label.addStyleName(style);
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
				label.addStyleName(style);
			}
		}
		return label;
	}
}
