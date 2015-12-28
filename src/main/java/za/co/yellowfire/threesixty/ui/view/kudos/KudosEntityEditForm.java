package za.co.yellowfire.threesixty.ui.view.kudos;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.Kudos;
import za.co.yellowfire.threesixty.domain.kudos.KudosRepository;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.ImageBuilder;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.button.CrudHeaderButtons;
import za.co.yellowfire.threesixty.ui.component.container.kudos.BadgeContainer;
import za.co.yellowfire.threesixty.ui.component.container.user.UserContainer;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.component.field.MRichTextArea;
import za.co.yellowfire.threesixty.ui.component.field.PictureField;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm.FileEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class KudosEntityEditForm extends AbstractEntityEditForm<Kudos> {
	private static final Logger LOG = LoggerFactory.getLogger(KudosEntityEditForm.class);
	
	@PropertyId("badge")
	private MComboBox badgeField = 
			new MComboBox(I8n.Kudos.Fields.BADGE)
			.withWidth(100.0f, Unit.PERCENTAGE);
		
	@PropertyId("recipient")
	private MComboBox recipientField = 
			new MComboBox(I8n.Kudos.Fields.RECIPIENT)
			.withWidth(100.0f, Unit.PERCENTAGE)
			.withReadOnly(true);
		
	@PropertyId("message")
	private MRichTextArea messageField = 
			new MRichTextArea(I8n.Kudos.Fields.MESSAGE)
			.withWidth(100.0f, Unit.PERCENTAGE);

	private PictureField pictureField = 
			new PictureField(I8n.Kudos.Fields.PICTURE, this::onSelectedPicture);
	
	private Grid receivedField;
	
    private GridFsClient client;
    private KudosRepository repository;
    private UserService userService;
    private CrudHeaderButtons headerButtons;
    
	public KudosEntityEditForm(
			final BadgeRepository badgeRepository, 
			final KudosRepository repository, 
			final UserService userService, 
			final GridFsClient client) {
		this.client = client;
		this.repository = repository;
		this.userService = userService;
		
		this.recipientField.setContainerDataSource(new UserContainer(userService, true));
		this.badgeField.setContainerDataSource(new BadgeContainer(badgeRepository));
	}
	
	public void setHeaderButtons(final CrudHeaderButtons headerButtons) {
		this.headerButtons = headerButtons;
	}
	
	@Override
	protected void internalLayout() {

		Label headerCaption = LabelBuilder.build("Give kudos where due", Style.Text.BOLDED, ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
		headerCaption.setSizeUndefined();
		
		HorizontalLayout header = 
				PanelBuilder.HORIZONTAL(Style.Kudos.Received.HEADER, 
				headerCaption,
				this.headerButtons);
		header.setSpacing(true);
		
		/* Layout the form */
		addComponent(PanelBuilder.FORM(
				header,
        		PanelBuilder.HORIZONTAL(badgeField, recipientField),
        		messageField
        ));
        addComponent(buildReceivedPanel());
	}
		
	private Grid buildReceivedTable() {
	
		if (receivedField == null) {
			receivedField = new Grid(new KudosContainer());
			receivedField.setColumnOrder("picture", "message");
			receivedField.getColumn("picture").setRenderer(new ImageRenderer()).setWidth(80.0f);
			receivedField.getColumn("message").setRenderer(new HtmlRenderer());
			receivedField.removeHeaderRow(0);
			receivedField.setWidth(100.0f, Unit.PERCENTAGE);
			receivedField.setHeight(100.0f, Unit.PERCENTAGE);
			receivedField.setStyleName("kudos-received");
			receivedField.setResponsive(true);
			receivedField.setCellStyleGenerator(cell -> (String) cell.getPropertyId());
		}
		return receivedField;
	}
	
	protected VerticalLayout buildReceivedPanel() {
		/* Build the received panel */
		//if (this.receivedPanel == null) {
			//this.receivedPanel = 
			Label headerCaption = 
					
					LabelBuilder.build(
							"Received", 
							Style.Text.BOLDED, ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
			headerCaption.setSizeUndefined();
			
			HorizontalLayout header = 
					PanelBuilder.HORIZONTAL(
							Style.Kudos.Received.HEADER, headerCaption);
			header.setSpacing(true);
			
			VerticalLayout panel = PanelBuilder.VERTICAL(header,buildReceivedTable());
		
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
		this.badgeField.refresh();
	
		/* Fill the recipient field with all the users except the current */
		this.recipientField.refresh();
		
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
	
	protected void onSelectedPicture(FileEvent event) {
		try {
			if (getValue() != null) {
				getValue().setPicture(event.getFile());
				displayPicture();
			}
		} catch (IOException e) {
			Notification.show("Error changing kudos picture", e.getMessage(), Type.ERROR_MESSAGE);
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
			this.pictureField.setSource(getValue().getPictureContent(), getValue().getPictureName() + "." + new Date().getTime() + " .png");
			this.pictureField.markAsDirty();
		}
	}

	private class KudosContainer extends BeanItemContainer<KudosItemModel> {

		public KudosContainer() throws IllegalArgumentException {
			super(KudosItemModel.class);
			
			addAllKudos(repository.findByRecipient(
					userService.getCurrentUser(),
					new Sort(Direction.DESC, "createdDate")));
		}
	
		public void addAllKudos(final Collection<Kudos> collection) {
			for(Kudos item : collection) {
				addBean(new KudosItemModel(item));
			}
		}
		
	}
}
