package za.co.yellowfire.threesixty.ui.view.kudos;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.*;
import com.vaadin.ui.renderers.HtmlRenderer;
import com.vaadin.ui.renderers.ImageRenderer;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.vaadin.viritin.layouts.MCssLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.*;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.resource.BadgeClientService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.button.CrudHeaderButtons;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class KudosEntityEditForm extends AbstractEntityEditForm<Kudos> {
	
	@PropertyId("badge")
	private ComboBox<Badge> badgeField =
			new ComboBox<>(I8n.Kudos.Fields.BADGE);

	@PropertyId("recipient")
	private ComboBox<User> recipientField =
			new ComboBox<>(I8n.Kudos.Fields.RECIPIENT);
		
	@PropertyId("message")
	private RichTextArea messageField =
			new RichTextArea(I8n.Kudos.Fields.MESSAGE);

	//private PictureField pictureField = 
	//		new PictureField(I8n.Kudos.Fields.PICTURE, this::onSelectedPicture);
	
	private Grid<Kudos> receivedField;
	
    private GridFsClient client;
    private KudosRepository repository;
    private UserService userService;
    private final BadgeClientService badgeClientService;
    private CrudHeaderButtons headerButtons;
    private KudosWallet wallet;
    private Label value;
    
	KudosEntityEditForm(
			final BadgeRepository badgeRepository, 
			final KudosRepository repository, 
			final UserService userService, 
			final BadgeClientService badgeClientService,
			final GridFsClient client) {
		this.client = client;
		this.repository = repository;
		this.userService = userService;
		this.badgeClientService = badgeClientService;

		this.recipientField.setWidth(100.0f, Unit.PERCENTAGE);
		this.recipientField.setReadOnly(true);
		this.recipientField.setDataProvider(new ListDataProvider<>(userService.findUsersExceptCurrent()));

		this.badgeField.setWidth(100.0f, Unit.PERCENTAGE);
		this.badgeField.setDataProvider(new ListDataProvider<>(badgeRepository.findByActive(true)));

		this.messageField.setWidth(100.0f, Unit.PERCENTAGE);
		this.wallet = repository.getWallet(userService.getCurrentUser());
	}
	
	void setHeaderButtons(final CrudHeaderButtons headerButtons) {
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
			receivedField = new Grid<>(new ListDataProvider<>(repository.findByRecipient(
					userService.getCurrentUser(),
					new Sort(Direction.DESC, "createdDate"))));
			receivedField.setColumnOrder("picture", "message");
			receivedField.getColumn("picture").setRenderer(new ImageRenderer()).setWidth(85.0f);
			receivedField.getColumn("message").setRenderer(new HtmlRenderer());
			receivedField.removeHeaderRow(0);
			receivedField.setWidth(100.0f, Unit.PERCENTAGE);
			receivedField.setStyleName("kudos-received");
			receivedField.setResponsive(false);
			receivedField.setStyleGenerator(cell -> (String) cell.getId());
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

		return PanelBuilder.VERTICAL(header,buildReceivedTable());
	}
	
	@Override
	protected void updateFieldConstraints() {
		super.updateFieldConstraints();
		
		/* Fill the recipient field with all the users except the current */
		this.badgeField.getDataProvider().refreshAll();;
	
		/* Fill the recipient field with all the users except the current */
		this.recipientField.getDataProvider().refreshAll();
		
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
	
	void onPostSave() {
		this.wallet = repository.getWallet(userService.getCurrentUser());
		
		value.setValue(getWalletValue());
		decorateWalletValue();
	}
}
