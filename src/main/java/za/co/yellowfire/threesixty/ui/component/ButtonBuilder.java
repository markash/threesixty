package za.co.yellowfire.threesixty.ui.component;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class ButtonBuilder {
	
	public static Button build(final String caption, final ClickListener listener) {
		return build(caption, (String)null, listener);
	}
	
	public static Button build(final String caption, final FontAwesome fontIcon, final ClickListener listener) {
		Button button = new Button(caption);
		button.setIcon(fontIcon);
		button.setWidth(100.0f, Unit.PERCENTAGE);
		if (listener != null) {
			button.addClickListener(listener);
		}
        return button;
	}
	
	public static Button build(final String caption, final FontAwesome fontIcon, final ClickListener listener, final String...styles) {
		Button button = build(caption, fontIcon, listener);
		if (styles != null && styles.length > 0) {
			for(String style : styles) {
				button.addStyleName(style);
			}
		}
        return button;
	}
	
	public static Button build(final String caption, final String styleName, final ClickListener listener) {
		Button button = new Button(caption);
		if (StringUtils.isNotBlank(styleName)) {
			button.setStyleName(styleName);
		}
		button.setWidth(100.0f, Unit.PERCENTAGE);
		if (listener != null) {
			button.addClickListener(listener);
		}
        return button;
	}
	
	public static Button SAVE(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(BUTTON_SAVE, FontAwesome.CHECK_CIRCLE, listener, styles);	
	}
	
	public static Button RESET(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(BUTTON_RESET, FontAwesome.REFRESH, listener, styles);	
	}
	
	public static Button NEW(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(BUTTON_NEW, FontAwesome.ASTERISK, listener, styles);	
	}
	
	public static Button DELETE(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(BUTTON_DELETE, FontAwesome.REMOVE, listener, styles);	
	}
	
	public static Button CHANGE(final ClickListener listener, final String...styles) {
		return ButtonBuilder.build(BUTTON_CHANGE, FontAwesome.UPLOAD, listener, styles);	
	}
	
	public static Button DASHBOARD_EDIT(final String id, final ClickListener listener) {
		Button button =  ButtonBuilder.build(BUTTON_EDIT, FontAwesome.EDIT, listener, "icon-edit", ValoTheme.BUTTON_ICON_ONLY);
		button.setId(id);
		button.setDescription("Edit Dashboard");
		return button;
	}
	
	protected static final String BUTTON_SAVE = "Save";
	protected static final String BUTTON_RESET = "Reset";
	protected static final String BUTTON_DELETE = "Delete";
	protected static final String BUTTON_NEW = "New...";
	protected static final String BUTTON_CHANGE = "Change...";
	protected static final String BUTTON_EDIT = "Edit...";
}

