package za.co.yellowfire.threesixty.ui.view.org;

import com.github.markash.ui.component.button.ButtonBuilder;
import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.AbstractDashboardView;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MPanel;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.organization.Identity;
import za.co.yellowfire.threesixty.domain.organization.IdentityService;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;


@Secured("ROLE_ADMIN")
@SideBarItem(sectionId = Sections.ADMINISTRATION, caption = IdentityView.TITLE, order = 3)
@VaadinFontIcon(VaadinIcons.GLOBE)
@SpringView(name = IdentityView.VIEW_NAME)
public class IdentityView extends AbstractDashboardView {
	private static final long serialVersionUID = 1L;

	public static final String TITLE = I8n.Identity.SINGULAR;
	public static final String VIEW_NAME = "identity";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
	public static final String TITLE_ID = VIEW_NAME + "-title";

	public static String VIEW(final String entity) {
		return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity);
	}

	private Tree<Identity> tree;
	private Panel panel = new MPanel();
	//private IdentityModel itemId = null;
	private IdentityEntityEditForm form = null;
	private OrganizationIconResolver iconResolver = new OrganizationIconResolver();
	private MVerticalLayout content = new MVerticalLayout().withMargin(false);

	private MButton saveButton = new MButton("Save").withIcon(VaadinIcons.CHECK_CIRCLE).withListener(this::onSave);
	private MButton resetButton = new MButton("Save").withIcon(VaadinIcons.REFRESH).withListener(this::onReset);
	private MButton createButton = new MButton("Save").withIcon(VaadinIcons.ASTERISK).withListener(this::onCreate);
	private MButton deleteButton = new MButton("Save").withIcon(VaadinIcons.TRASH).withListener(this::onDelete);

	//private Button resetButton = ButtonBuilder.RESET(this::onReset);
	//private Button createButton = ButtonBuilder.NEW(this::onCreate);
	//private Button deleteButton = ButtonBuilder.DELETE(this::onDelete);
	
    //private Button[] buttons = new Button[] {saveButton, resetButton, createButton, deleteButton};

	private IdentityService identityService;
    private UserService userService;

	@Autowired
	public IdentityView(
			final IdentityService identityService,
			final UserService userService,
			final IdentityDataProvider dataProvider) {

		super(TITLE);
		this.userService = userService;
		this.identityService = identityService;

		this.tree = new Tree<>(dataProvider);
		tree.addItemClickListener(this::nodeClicked);

		getToolbar().addAction(saveButton);
		getToolbar().addAction(resetButton);
		getToolbar().addAction(createButton);
		getToolbar().addAction(deleteButton);
	}


	protected Component buildContent() {



//		HorizontalLayout layout = new HorizontalLayout();
//		layout.setMargin(false);
//		layout.setSpacing(true);
//		layout.setWidth(Style.Percentage._100);
//		layout.addComponent(this.tree);
//

		this.content.setWidth(100.0f, Unit.PERCENTAGE);
		this.panel.setPrimaryStyleName("organization-panel");
		this.panel.setWidth(100.0f, Unit.PERCENTAGE);
		//this.content.addComponent(buildPanelHeader());
		this.content.addComponent(this.panel);

		MHorizontalLayout layout = new MHorizontalLayout()
				.withFullWidth()
				.with(this.tree, this.content);

		layout.setExpandRatio(this.tree, 1);
		layout.setExpandRatio(this.content, 2);
		return layout;
	}

//	private void buildNode(
//			final Identity node) {
//
//		buildNode(new IdentityModel(node, iconResolver), null);
//	}

//	private void buildNode(
//			final IdentityModel node,
//			final IdentityModel parent) {
//
//		tree.addItem(node);
//		tree.setItemIcon(node, node.getIcon());
//
//		if (parent != null) {
//			node.setForm(buildOrganization(node));
//			tree.setParent(node, parent);
//		} else {
//			node.setForm(buildDivision(node));
//		}
//
//		if (node.hasChildren()) {
//			for (IdentityModel child : node.getChildren()) {
//				buildNode(child, node);
//			}
//		} else {
//			tree.setChildrenAllowed(node, false);
//		}
//	}

//	private HorizontalLayout buildPanelHeader() {
//
//		MLabel header = new MLabel(VaadinIcons.SITEMAP.getHtml() +  " Identity Level")
//				.withContentMode(ContentMode.HTML)
//				.withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);
//
//		return new MHorizontalLayout()
//				.withFullWidth()
//				.withStyleName(Style.Organization.HEADER)
//				.with(header);
//	}
	
//	private FormLayout buildOrganization(IdentityModel node) {
//		FormLayout layout = new FormLayout();
//		layout.setPrimaryStyleName("organization-panel");
//		layout.setMargin(false);
//		layout.setSpacing(false);
//
//		layout.addComponent(new ComboBox("Levels", Arrays.asList(1, 2, 3, 4, 5)));
//		return layout;
//	}
//
//	private FormLayout buildDivision(IdentityModel node) {
//		FormLayout layout = new FormLayout();
//		layout.setPrimaryStyleName("organization-panel");
//		layout.setMargin(false);
//		layout.setSpacing(false);
//
//		layout.addComponent(new TextField("Name", node.getName()));
//		return layout;
//	}

	private void loadForm(
			final Identity identity) {

		this.form = new IdentityEntityEditForm(userService, iconResolver);
		this.form.setValue(identity);
		this.form.layout();
		this.form.setWidth(100.0f, Unit.PERCENTAGE);
		this.panel.setContent(this.form);
		
		this.createButton.setEnabled((identity.getMetadata().isPresent() && identity.getMetadata().get().hasChild()));
	}
	
	private void deleteNode() {
//		if (this.itemId == null) { return; }
//
//		/* Delete the organisation level */
//		this.identityService.delete(this.itemId.getIdentity());
//		/* Select the parent of the level */
//		IdentityModel parent = (IdentityModel) tree.getParent(itemId);
//		/* Remove from the tree */
//		tree.removeItem(this.itemId);
//		/* Maintain the current level to the parent */
//		if (parent != null) {
//			tree.select(parent);
//			/* Change the selected node to the created node */
//			this.itemId = parent;
//			/* Load the node form */
//			loadForm(parent);
//		}
	}

	private void resetNode() {
//		if (this.form == null) { return; }
//
//		this.form.discard();
	}
	
	@Override
	public void enter(
			final ViewChangeEvent event) {


	}
	
	private void nodeClicked(
			final Tree.ItemClick<Identity> event) {

		/* Remember the clicked item model */
		//this.itemId = event.getItem();
		/* Load the form for the model */
		loadForm(event.getItem());
	}

	@SuppressWarnings("unused")
	private void onSave(
			final Button.ClickEvent event) {

		if (this.form == null) { return; }

		try {
			//Commit the form data & validate
			this.form.commit();
	        //Persist the outcome
	        Identity result = this.identityService.save(form.getValue());
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", getTitle() + " updated successfully.", 2000);
	        // Refresh the tree by marking it as dirty
	        tree.markAsDirty();
		} catch (Exception exception) {
            Notification.show("Error while updating", Notification.Type.ERROR_MESSAGE);
        }
	}

	@SuppressWarnings("unused")
	private void onReset(
			final Button.ClickEvent event) {

		ConfirmDialog.show(
				UI.getCurrent(),
				"Confirmation",
				"Are you sure that you would like reset?",
				"Yes",
				"No",
				dialog -> { if (dialog.isConfirmed()) resetNode(); }
				);
	}

	@SuppressWarnings("unused")
	private void onCreate(
			final Button.ClickEvent event) {

		if (this.form == null) { return; }

		try {
			/* Create the child node */
			Identity result = this.identityService.createChildFor(this.form.getValue());
			/* Load the form for the model */
			loadForm(result);
//
//			IdentityModel node = new IdentityModel(result, iconResolver);
//			/* Build the node in the tree */
//			buildNode(node, itemId);

			/* Select the node in the tree */
			tree.select(result);

//			/* Change the selected node to the created node */
//			this.itemId = node;
//			/* Load the node form */
//			loadForm(node);

	        /* Notify the user of the outcome */
	        NotificationBuilder.showNotification("New", "Created new organization level.", 2000);
	        /* Refresh the tree by marking it as dirty */
	        tree.markAsDirty();
		} catch (Exception exception) {
            Notification.show("Error while creating", Notification.Type.ERROR_MESSAGE);
        }
	}
	
	private void onDelete(
			final Button.ClickEvent event) {

		ConfirmDialog.show(
				UI.getCurrent(),
				"Confirmation",
				"Are you sure that you would like to delete this organization level including the linked levels?",
				"Yes",
				"No",
				dialog -> { if (dialog.isConfirmed()) deleteNode(); }
				);
	}
}
