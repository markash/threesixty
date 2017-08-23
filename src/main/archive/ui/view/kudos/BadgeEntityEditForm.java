package za.co.yellowfire.threesixty.ui.view.kudos;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.viritin.fields.IntegerField;
import org.vaadin.viritin.fields.MTextField;
import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.Ideal;
import za.co.yellowfire.threesixty.domain.kudos.IdealRepository;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.ByteArrayStreamResource;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm.FileEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

import java.io.IOException;

public class BadgeEntityEditForm extends AbstractEntityEditForm<Badge> {
	private static final long serialVersionUID = 1L;
	private static final String WINDOW_PICTURE = "Badge picture";

	private MTextField descriptionField = new MTextField(I8n.Badge.Fields.DESCRIPTION).withFullWidth();
	private ComboBox<Ideal> idealField;
	private TextArea motivationField = new TextArea(I8n.Badge.Fields.MOTIVATION);
	private IntegerField valueField = new IntegerField(I8n.Badge.Fields.VALUE).withFullWidth();
	
	//TODO Make this into a component
	private Image pictureField = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
    private Window pictureWindow = new Window(WINDOW_PICTURE, new PictureSelectionForm(this::onSelectedPicture));
    private Button pictureButton = ButtonBuilder.CHANGE(this::onChangePicture);
    
    private GridFsClient client;
    
    @Autowired
	BadgeEntityEditForm(
			final BadgeRepository repository, 
			final IdealRepository idealRepository,
			final GridFsClient client) {

        super(Badge.class);

		this.client = client;
		
		this.idealField = new ComboBox<>(I8n.Badge.Fields.IDEAL, idealRepository.findByActive(true));
        this.idealField.setWidth(Style.Percentage._100);
		
		this.pictureField.setHeight(150.0f, Unit.PIXELS);
		this.pictureButton.setWidth(this.pictureField.getWidth(), this.pictureField.getWidthUnits());

		this.motivationField.setWidth(Style.Percentage._100);
		//this.motivationField.setNullRepresentation("");
		this.motivationField.setRows(7);

		getBinder().bind(descriptionField, "description");
		getBinder().bind(idealField, "ideal");
		getBinder().bind(motivationField, "motivation");
		getBinder().bind(valueField, "value");
	}
	
	@Override
	protected void internalLayout() {
		
		addComponent(PanelBuilder.FORM(
        		PanelBuilder.HORIZONTAL(
        				PanelBuilder.VERTICAL_CENTERED(pictureField, pictureButton), 
        				PanelBuilder.VERTICAL(idField, descriptionField, idealField, valueField)
        		)
        ));
        addComponent(motivationField);
	}
	
//	@Override
//	protected void updateFieldConstraints() {
//		super.updateFieldConstraints();
//
//		fetchPicture();
//		displayPicture();
//	}

	@Override
	protected Badge buildEmpty() {
		return Badge.EMPTY();
	}
	
	private void onChangePicture(ClickEvent event) {
		UI.getCurrent().addWindow(pictureWindow);
	}
	
	private void onSelectedPicture(FileEvent event) {
		try {
			if (getValue() != null) {
				getValue().setPicture(event.getFile());
				displayPicture();
			}
		} catch (IOException e) {
			Notification.show("Error changing badge picture", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	void onPostSave(final ClickEvent event) {
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
	
	private void displayPicture() {
		
		if (getValue().hasPicture()) {
			this.pictureField.setSource(new ByteArrayStreamResource(getValue().getPictureContent(), getValue().getPictureName()));
			this.pictureField.markAsDirty();
		}
	}
}
