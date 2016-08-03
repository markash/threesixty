package za.co.yellowfire.threesixty.ui.view.kudos;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;

import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.IdealRepository;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.ByteArrayStreamResource;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm.FileEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

public class BadgeEntityEditForm extends AbstractEntityEditForm<Badge> {
	private static final long serialVersionUID = 1L;
	private static final String WINDOW_PICTURE = "Badge picture";
	
	@PropertyId("description")
	private MTextField descriptionField = 
		new MTextField(I8n.Fields.DESCRIPTION)
			.withFullWidth()
			.withNullRepresentation("");
	
	@PropertyId("ideal")
	private MComboBox idealField;
	
	@PropertyId("motivation")
	private MTextArea motivationField = new MTextArea(I8n.Fields.MOTIVATION)
			.withFullWidth()
			.withNullRepresentation("")
			.withRows(7);

	//TODO Make this into a component
	private Image pictureField = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
    private Window pictureWindow = new Window(WINDOW_PICTURE, new PictureSelectionForm(this::onSelectedPicture));
    private Button pictureButton = ButtonBuilder.CHANGE(this::onChangePicture);
    
    private GridFsClient client;
    
    @Autowired
	public BadgeEntityEditForm(
			final BadgeRepository repository, 
			final IdealRepository idealRepository,
			final GridFsClient client) {
		this.client = client;
		
		this.idealField = new MComboBox(new IndexedContainer(idealRepository.findByActive(true)))
				.withWidth(Style.Percentage._100);
		
		this.pictureField.setHeight(150.0f, Unit.PIXELS);
		this.pictureField.setImmediate(true);
		this.pictureButton.setWidth(this.pictureField.getWidth(), this.pictureField.getWidthUnits());
	}
	
	@Override
	protected void internalLayout() {
		
		addComponent(PanelBuilder.FORM(
        		PanelBuilder.HORIZONTAL(
        				PanelBuilder.VERTICAL_CENTERED(pictureField, pictureButton), 
        				PanelBuilder.VERTICAL(idField, descriptionField, idealField)
        		)
        ));
        addComponent(motivationField);
	}
	
	@Override
	protected void updateFieldContraints() {
		super.updateFieldContraints();
		
		fetchPicture();
		displayPicture();
	}

	@Override
	protected Badge buildEmpty() {
		return Badge.EMPTY();
	}
	
	protected void onChangePicture(ClickEvent event) {
		UI.getCurrent().addWindow(pictureWindow);
	}
	
	protected void onSelectedPicture(FileEvent event) {
		try {
			if (getValue() != null) {
				getValue().setPicture(event.getFile());
				displayPicture();
			}
		} catch (IOException e) {
			Notification.show("Error changing badge picture", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	protected void onPostSave(final ClickEvent event) {
		try {
			if (getValue() != null) {
				getValue().storePicture(client);
			}
		} catch (IOException e) {
			Notification.show("Store badge picture error", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	protected void fetchPicture() {
		try {
			if (getValue() != null) {
				getValue().retrievePicture(client);
			}
		} catch (IOException e) {
			Notification.show("Retrieve badge picture error", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	protected void displayPicture() {
		
		if (getValue().hasPicture()) {
			this.pictureField.setSource(new ByteArrayStreamResource(getValue().getPictureContent(), getValue().getPictureName()));
			this.pictureField.markAsDirty();
		}
	}
}
