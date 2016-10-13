package za.co.yellowfire.threesixty.ui.view.kudos;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.Kudos;
import za.co.yellowfire.threesixty.domain.kudos.KudosRepository;
import za.co.yellowfire.threesixty.domain.kudos.KudosWallet;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.resource.BadgeClientService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.button.CrudHeaderButtons;
import za.co.yellowfire.threesixty.ui.component.container.kudos.BadgeContainer;
import za.co.yellowfire.threesixty.ui.component.container.user.UserContainer;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.component.field.MRichTextArea;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class KudosEntityEditForm extends AbstractEntityEditForm<Kudos> {
	
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

	//private PictureField pictureField = 
	//		new PictureField(I8n.Kudos.Fields.PICTURE, this::onSelectedPicture);
	
	private Grid receivedField;
	
    private GridFsClient client;
    private KudosRepository repository;
    private UserService userService;
    private final BadgeClientService badgeClientService;
    private CrudHeaderButtons headerButtons;
    private KudosWallet wallet;
    private Label value;
    
	public KudosEntityEditForm(
			final BadgeRepository badgeRepository, 
			final KudosRepository repository, 
			final UserService userService, 
			final BadgeClientService badgeClientService,
			final GridFsClient client) {
		this.client = client;
		this.repository = repository;
		this.userService = userService;
		this.badgeClientService = badgeClientService;
		
		this.recipientField.setContainerDataSource(new UserContainer(userService, true));
		this.badgeField.setContainerDataSource(new BadgeContainer(badgeRepository));
		
		this.wallet = repository.getWallet(userService.getCurrentUser());
	}
	
	public void setHeaderButtons(final CrudHeaderButtons headerButtons) {
		this.headerButtons = headerButtons;
	}
	
	@Override
	protected void internalLayout() {

		//Label walletCaption = LabelBuilder.build("Wallet Balance: $" + wallet.getBalance().getValue(), Style.Text.BOLDED, ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
		
		Label headerCaption = LabelBuilder.build("Give kudos where due", Style.Text.BOLDED, ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
		headerCaption.setSizeUndefined();
		
		HorizontalLayout header = 
				PanelBuilder.HORIZONTAL(Style.Kudos.Received.HEADER, 
				headerCaption,
				this.headerButtons);
		header.setSpacing(true);
		
		/* Layout the form */
		addComponent(
				new MVerticalLayout(
						buildWalletTotal(),
						new MHorizontalLayout(
							PanelBuilder.FORM(
									header,
					        		PanelBuilder.HORIZONTAL(badgeField, recipientField),
					        		messageField
					        ),
							buildReceivedPanel()
						))
						.withResponsive(true)
						.withFullWidth()
				.withMargin(false)
				.withFullWidth()
				);
				
//	
//		addComponent(PanelBuilder.FORM(
//				header,
//        		PanelBuilder.HORIZONTAL(badgeField, recipientField),
//        		messageField
//        ));
//        addComponent(buildReceivedPanel());
	}
	
	private MCssLayout buildWalletTotal() {
		List<String> styles = Arrays.asList(ValoTheme.LABEL_H2, ValoTheme.LABEL_NO_MARGIN);
		Label label = LabelBuilder.build("Wallet Balance: ", "wallet-label", styles);
		
		value = LabelBuilder.build(
				getWalletValue(), 
				styles);
		decorateWalletValue();
		
		return new MCssLayout(label, value)
				.withStyleName("wallet-panel")
				.withFullWidth();
	}
	
	private String getWalletValue() {
		return "$ " + Math.abs(wallet.getBalance().getValue());
	}
	
	private void decorateWalletValue() {
		value.removeStyleName("wallet-positive-value");
		value.removeStyleName("wallet-negative-value");
		value.addStyleName(wallet.getBalance().getValue() >= 0 ? "wallet-positive-value" : "wallet-negative-value");
	}
	
	private Grid buildReceivedTable() {
	
		if (receivedField == null) {
			receivedField = new Grid(new KudosContainer(badgeClientService));
			receivedField.setColumnOrder("picture", "message");
			receivedField.getColumn("picture").setRenderer(new ImageRenderer()).setWidth(85.0f);
			receivedField.getColumn("message").setRenderer(new HtmlRenderer());
			receivedField.removeHeaderRow(0);
			receivedField.setWidth(100.0f, Unit.PERCENTAGE);
			receivedField.setStyleName("kudos-received");
			receivedField.setResponsive(false);
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
		//fetchPicture();
		//displayPicture();
	}

	@Override
	protected Kudos buildEmpty() {
		Kudos kudos = Kudos.EMPTY();
		kudos.setDonor(userService.getCurrentUser());
		return kudos;
	}
	
	public void onPostSave() {
		this.wallet = repository.getWallet(userService.getCurrentUser());
		
		value.setValue(getWalletValue());
		decorateWalletValue();
	}
	
	private class KudosContainer extends BeanItemContainer<KudosItemModel> {
		private final BadgeClientService service;
		
		public KudosContainer(final BadgeClientService service) throws IllegalArgumentException {
			super(KudosItemModel.class);
			
			this.service = service;
			
			addAllKudos(repository.findByRecipient(
					userService.getCurrentUser(),
					new Sort(Direction.DESC, "createdDate")));
		}
	
		public void addAllKudos(final Collection<Kudos> collection) {
			for(Kudos item : collection) {
				addBean(new KudosItemModel(service, item));
			}
		}
		
	}
}
