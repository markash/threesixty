package za.co.yellowfire.threesixty.ui.view.kudos;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.Kudos;
import za.co.yellowfire.threesixty.domain.kudos.KudosRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.ByteArrayStreamResource;
import za.co.yellowfire.threesixty.ui.component.ImageBuilder;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.PictureSelectionForm;
import za.co.yellowfire.threesixty.ui.component.PictureSelectionForm.FileEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class KudosEntityEditForm extends AbstractEntityEditForm<Kudos> {
	
	private static final String WINDOW_PICTURE = "Badge picture";
	
	@PropertyId("badge")
	private ComboBox badgeField;
	
	@PropertyId("recipient")
	private ComboBox recipientField;
	
	@PropertyId("message")
	private RichTextArea messageField;
	
	//TODO Make this into a component
	private Image pictureField = ImageBuilder.BLANK();
    private Window pictureWindow = new Window(WINDOW_PICTURE, new PictureSelectionForm(this::onSelectedPicture));
    private Button pictureButton = ButtonBuilder.CHANGE(this::onChangePicture);
    
    //private VerticalLayout receivedPanel;
    
    private GridFsClient client;
    private KudosRepository repository;
    private UserService userService;
    private BadgeRepository badgeRepository;
    
	public KudosEntityEditForm(
			final BadgeRepository badgeRepository, 
			final KudosRepository repository, 
			final UserService userService, 
			final GridFsClient client) {
		this.client = client;
		this.repository = repository;
		this.userService = userService;
		this.badgeRepository = badgeRepository;
		
		this.badgeField = buildBadgeField();
		this.recipientField = buildRecipientField();
		this.messageField = buildMessageField();
		
		this.pictureField.setHeight(150.0f, Unit.PIXELS);
		this.pictureField.setImmediate(true);
		this.pictureButton.setWidth(this.pictureField.getWidth(), this.pictureField.getWidthUnits());
	}
	
	@Override
	protected void internalLayout() {

		/* Layout the form */
		addComponent(PanelBuilder.FORM(
				LabelBuilder.build("Give kudos where kudos is due", Style.Kudos.Received.HEADER, Style.Text.BOLDED),
        		PanelBuilder.HORIZONTAL(badgeField, recipientField),
        		messageField
        ));
        addComponent(buildReceivedPanel());
	}
	
	private ComboBox buildBadgeField() {
		ComboBox field = new ComboBox(I8n.Kudos.Fields.BADGE, new IndexedContainer());
		field.setWidth(100.0f, Unit.PERCENTAGE);
		return field;
	}
	
	private ComboBox buildRecipientField() {
		ComboBox field = new ComboBox(I8n.Kudos.Fields.RECIPIENT, new IndexedContainer());
		field.setWidth(100.0f, Unit.PERCENTAGE);
		field.setEnabled(false);
		return field;
	}
	
	private RichTextArea buildMessageField() {
		RichTextArea field = new RichTextArea(I8n.Kudos.Fields.MESSAGE);
		field.setWidth(100.0f, Unit.PERCENTAGE);
		field.setNullRepresentation("");
		return field;
	}
	
	protected VerticalLayout buildReceivedPanel() {
		/* Build the received panel */
		//if (this.receivedPanel == null) {
			//this.receivedPanel = 
			VerticalLayout panel = PanelBuilder.VERTICAL(
							null,
							LabelBuilder.build("Received", Style.Kudos.Received.HEADER, Style.Text.BOLDED), 
							buildReceivedItems());
			
		//} else {
//			if (this.receivedPanel.isAttached()) {
//				this.receivedPanel.detach();
//			}
//			this.receivedPanel.removeAllComponents();
//			this.receivedPanel.addComponent(LabelBuilder.build("Received", Style.Kudos.Received.HEADER));
//			
//			for (HorizontalLayout c : buildReceivedItems()) {
//				this.receivedPanel.addComponent(c);
//			}
		//}
		
		return panel;
	}
	
	protected HorizontalLayout[] buildReceivedItems() {
		
		HorizontalLayout[] items;
		
		List<Kudos> received = this.repository.findByRecipient(
				userService.getCurrentUser(),
				new Sort(Direction.DESC, "createdDate"));
		
		if (received != null && received.size() > 0) {
			if (received.size() > 10) {
				received = received.subList(0, 10);
			}
			
			int i = 0;
			items = new HorizontalLayout[received.size()];
			for(Kudos kudos : received) {
				
				if (kudos.getBadge() != null) { 
					try {
						kudos.getBadge().retrievePicture(client);
					} catch (IOException e) {
						System.out.println("Unable retrieve kudos badge: " + kudos.getBadge().getPictureName());
					}
				}
				
				Image badgeField = ImageBuilder.build(kudos.getBadge());
				Label messageField = LabelBuilder.build(kudos.getMessage(), ContentMode.HTML);
				
				badgeField.addStyleName(Style.Kudos.Received.IMAGE);
				HorizontalLayout item = PanelBuilder.HORIZONTAL(Style.Kudos.Received.ITEM, badgeField, messageField);
				item.setExpandRatio(badgeField, 1.0f);
				item.setExpandRatio(messageField, 8.0f);
				
				item.setSpacing(true);
				item.setMargin(false);
				
				if (i % 2 == 0) {
					item.addStyleName(Style.Kudos.Received.HEADER);
				}
				
				items[i++] = item;
			}
		} else {
			items = new HorizontalLayout[1];
			items[0] = PanelBuilder.HORIZONTAL(Style.Kudos.Received.ITEM, LabelBuilder.build(I8n.Kudos.Received.NONE, Style.Text.BOLDED, Style.Text.ITALICIZED));
		}
		
		return items;
	}
	
	@Override
	protected void updateFieldContraints() {
		super.updateFieldContraints();
		
		/* Fill the recipient field with all the users except the current */
		this.badgeField.getContainerDataSource().removeAllItems();
		for (Badge badge : badgeRepository.findByActive(true)) {
			this.badgeField.getContainerDataSource().addItem(badge);
		}
		
		/* Fill the recipient field with all the users except the current */
		this.recipientField.getContainerDataSource().removeAllItems();
		for (User user : userService.findUsersExcept(userService.getCurrentUser())) {
			this.recipientField.getContainerDataSource().addItem(user);
		}
		
		/* Rebuild the received items panel */
		buildReceivedPanel();
		
		/* Update the picture */
		fetchPicture();
		displayPicture();
	}

	@Override
	protected Kudos buildEmpty() {
		return Kudos.EMPTY();
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
			this.pictureField.setSource(new ByteArrayStreamResource(getValue().getPictureContent(), getValue().getPictureName() + "." + new Date().getTime() + " .png"));
			this.pictureField.markAsDirty();
		}
	}
}
