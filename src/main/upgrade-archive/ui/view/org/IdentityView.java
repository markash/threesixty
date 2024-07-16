package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.ItemClickEvent;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import za.co.yellowfire.threesixty.domain.organization.Identity;
import za.co.yellowfire.threesixty.domain.organization.IdentityService;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;

@Secured("ROLE_ADMIN")
//@SideBarItem(sectionId = Sections.ADMINISTRATION, caption = IdentityView.TITLE, order = 3)
@Menu(icon = "line-awesome/svg/pencil-ruler-solid.svg", order = 0)
@Uses(Icon.class)
@Route(value = IdentityView.VIEW_NAME)
public class IdentityView extends Composite<VerticalLayout> {

	public static final String TITLE = I8n.Identity.SINGULAR;
	public static final String VIEW_NAME = "identity";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
	public static final String TITLE_ID = VIEW_NAME + "-title";

	public static String VIEW(final String entity) {
		return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity);
	}

	private TreeGrid<Identity> tree;
	private HorizontalLayout panel = new HorizontalLayout();
	//private IdentityModel itemId = null;
	private IdentityEntityEditForm form = null;
	private OrganizationIconResolver iconResolver = new OrganizationIconResolver();
	private final VerticalLayout content = new VerticalLayout();

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

		this.content.setMargin(false);

		this.tree = new TreeGrid<>(dataProvider);
		tree.addItemClickListener(this::nodeClicked);

		getToolbar().addAction(saveButton);
		getToolbar().addAction(resetButton);
		getToolbar().addAction(createButton);
		getToolbar().addAction(deleteButton);
	}


	protected Component buildContent() {


		VerticalLayout content = new VerticalLayout();
		content.setWidth(100.0f, Unit.PERCENTAGE);

		this.panel.setClassName("organization-panel");
		this.panel.setWidth(100.0f, Unit.PERCENTAGE);
		//this.content.addComponent(buildPanelHeader());
		this.content.add(this.panel);

		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidthFull();
		layout.add(this.tree, this.content);
		layout.setFlexGrow(1, (Component)this.tree);
		layout.setFlexGrow(2, this.content);
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
		//this.form.layout();
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
			final ItemClickEvent<Identity> event) {

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
	        // Refresh the tree by marking it as dirty TODO Resolve the markAsDirty
	        //tree.markAsDirty();
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
	        /* Refresh the tree by marking it as dirty TODO Resolve markAsDirty*/
	        //tree.markAsDirty();
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
