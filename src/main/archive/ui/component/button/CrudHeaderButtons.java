package za.co.yellowfire.threesixty.ui.component.button;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.themes.ValoTheme;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;

@SuppressWarnings("serial")
public class CrudHeaderButtons extends HeaderButtons {

    private Button saveButton;
	private Button resetButton;
	private Button createButton;
	private Button deleteButton;
	
    private CrudHeaderButtonConfig config;
        
    public CrudHeaderButtons(
			final ClickListener saveListener,
			final ClickListener resetListener,
			final ClickListener createListener,
			final ClickListener deleteListener,
			final Component...components) {
    	this(saveListener, resetListener, createListener, deleteListener, new CrudHeaderButtonConfig(), components);
    }
    
	public CrudHeaderButtons(
			final ClickListener saveListener,
			final ClickListener resetListener,
			final ClickListener createListener,
			final ClickListener deleteListener,
			final CrudHeaderButtonConfig config,
			final Component...components) {
		
		this.config = config;
		this.saveButton = ButtonBuilder.SAVE(saveListener);
		this.resetButton = ButtonBuilder.RESET(resetListener);
		this.createButton = ButtonBuilder.NEW(createListener);
		this.deleteButton = ButtonBuilder.DELETE(deleteListener);
		
		for (Component button : components) {
			addComponent(button);
		}
		
		setShow();
		setSpacing(true);
        addStyleName("toolbar");
	}
	
	public CrudHeaderButtonConfig getConfig() {
		return this.config;
	}
	
	public void setConfig(final CrudHeaderButtonConfig config) {
		this.config = config;
		setShow();
		setShowIconOny();
	}
	
	public CrudHeaderButtons disableSave() { this.saveButton.setEnabled(false); return this; }
	public CrudHeaderButtons disableReset() { this.resetButton.setEnabled(false); return this; }
	public CrudHeaderButtons disableCreate() { this.createButton.setEnabled(false); return this; }
	public CrudHeaderButtons disableDelete() { this.deleteButton.setEnabled(false); return this; }
	
	public CrudHeaderButtons enableSave() { this.saveButton.setEnabled(true); return this; }
	public CrudHeaderButtons enableReset() { this.resetButton.setEnabled(true); return this; }
	public CrudHeaderButtons enableCreate() { this.createButton.setEnabled(true); return this; }
	public CrudHeaderButtons enableDelete() { this.deleteButton.setEnabled(true); return this; }
	
	public CrudHeaderButtons saveShowIconOnly() { this.saveButton.setStyleName(ValoTheme.BUTTON_ICON_ONLY); return this; }
	public CrudHeaderButtons resetShowIconOnly() { this.resetButton.setStyleName(ValoTheme.BUTTON_ICON_ONLY); return this; }
	public CrudHeaderButtons createShowIconOnly() { this.createButton.setStyleName(ValoTheme.BUTTON_ICON_ONLY); return this; }
	public CrudHeaderButtons deleteShowIconOnly() { this.deleteButton.setStyleName(ValoTheme.BUTTON_ICON_ONLY); return this; }
	
	public Button getSaveButton() { return this.saveButton; }
	public Button getResetButton() { return this.resetButton; }
	public Button getCreateButton() { return this.createButton; }
	public Button getDeleteButton() { return this.deleteButton; }
	
	private void setShow() {
		removeComponent(saveButton);
		removeComponent(resetButton);
		removeComponent(createButton);
		removeComponent(deleteButton);
		
		if (config.isShowSave()) {
			addComponent(saveButton);
		}
		if (config.isShowReset()) {
			addComponent(resetButton);
		}
		if (config.isShowNew()) {
			addComponent(createButton);
		}
		if (config.isShowDelete()) {
			addComponent(deleteButton);
		}
	}
	
	private void setShowIconOny() {
		saveButton.removeStyleName(ValoTheme.BUTTON_ICON_ONLY);
		resetButton.removeStyleName(ValoTheme.BUTTON_ICON_ONLY);
		createButton.removeStyleName(ValoTheme.BUTTON_ICON_ONLY);
		deleteButton.removeStyleName(ValoTheme.BUTTON_ICON_ONLY);
		
		if (config.isShowSaveIconOnly()) {
			saveButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		}
		if (config.isShowResetIconOnly()) {
			resetButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		}
		if (config.isShowNewIconOnly()) {
			createButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		}
		if (config.isShowDeleteIconOnly()) {
			deleteButton.addStyleName(ValoTheme.BUTTON_ICON_ONLY);
		}
	}
}
