package za.co.yellowfire.threesixty.ui.view.org;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.event.ItemClickEvent;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.domain.organization.Organization;
import za.co.yellowfire.threesixty.domain.organization.OrganizationService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.button.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.view.AbstractDashboardPanel;

@SpringView(name = OrganizationView.VIEW_NAME)
public class OrganizationView extends AbstractDashboardPanel {
	

	private static final long serialVersionUID = 1L;

	public static final String TITLE = I8n.Organization.SINGULAR;
	public static final String VIEW_NAME = "organization";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
	public static final String TITLE_ID = VIEW_NAME + "-title";

	public static final String VIEW(final String entity) {
		return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity);
	}

	public static final String VIEW_NEW() {
		return VIEW("new_" + VIEW_NAME);
	}

	private Tree tree;
	private Panel panel = new Panel();
	private VerticalLayout content = new VerticalLayout();
	
	private Button saveButton = ButtonBuilder.SAVE(this::onSave);
	private Button resetButton = ButtonBuilder.RESET(this::onReset);
	private Button createButton = ButtonBuilder.NEW(this::onCreate);	
	
    private Button[] buttons = new Button[] {saveButton, resetButton, createButton};
    
	private OrganizationService organizationService;

	@Autowired
	public OrganizationView(final OrganizationService organizationService) {
		this.organizationService = organizationService;
	}

	@Override
	protected String getTitle() {
		return TITLE;
	}

	@Override
	protected String getTitleId() {
		return TITLE_ID;
	}

	@Override
	protected String getEditId() {
		return EDIT_ID;
	}

	public OrganizationView() {
		super();
	}

	@Override
	protected Component getHeaderButtons() {
		return new HeaderButtons(buttons);
	}
	
	protected Component buildContent() {

		HorizontalLayout layout = new HorizontalLayout();
		layout.setMargin(false);
		layout.setSpacing(true);
		layout.setWidth(Style.Percentage._100);
		
		buildTree();
		layout.addComponent(this.tree);
		
		this.content.setWidth(Style.Percentage._100);
		this.panel.setWidth(Style.Percentage._100);
		this.content.addComponent(buildPanelHeader());
		this.content.addComponent(this.panel);
		layout.addComponent(this.content);

		layout.setExpandRatio(this.tree, 1);
		layout.setExpandRatio(this.content, 2);
		return layout;
	}

	protected Tree buildTree() {
		if (tree == null) {
			tree = new Tree();
			tree.addItemClickListener(this::nodeClicked);
		}
		organizationService.retrieveRoots().forEach(root -> buildNode(root));
		return tree;
	}

	private void buildNode(final Organization node) {
		buildNode(new OrganizationModel(node), null);
	}

	private void buildNode(OrganizationModel node, OrganizationModel parent) {
		tree.addItem(node);
		tree.setItemIcon(node, node.getIcon());

		if (parent != null) {
			node.setForm(buildOrganization(node));
			tree.setParent(node, parent);
		} else {
			node.setForm(buildDivision(node));
		}

		if (node.hasChildren()) {
			for (OrganizationModel child : node.getChildren()) {
				buildNode(child, node);
			}
		} else {
			tree.setChildrenAllowed(node, false);
		}
	}

	private HorizontalLayout buildPanelHeader() {
		HorizontalLayout header = PanelBuilder.HORIZONTAL(Style.Organization.HEADER, 
				LabelBuilder.build(FontAwesome.SITEMAP.getHtml() +  " Organization Level", 
						ContentMode.HTML, 
						ValoTheme.LABEL_H3, 
						ValoTheme.LABEL_NO_MARGIN));
		
		return header;
	}
	
	private FormLayout buildOrganization(OrganizationModel node) {
		FormLayout layout = new FormLayout();
		layout.setPrimaryStyleName("organization-panel");
		layout.setMargin(false);
		layout.setSpacing(false);

		layout.addComponent(new MComboBox("Levels", Arrays.asList(1, 2, 3, 4, 5)));
		return layout;
	}

	private FormLayout buildDivision(OrganizationModel node) {
		FormLayout layout = new FormLayout();
		layout.setPrimaryStyleName("organization-panel");
		layout.setMargin(false);
		layout.setSpacing(false);

		layout.addComponent(new TextField("Name", node.getName()));
		return layout;
	}

	@Override
	public void enter(final ViewChangeEvent event) {
		build();
	}
	
	protected void nodeClicked(ItemClickEvent event) {
		OrganizationModel model = (OrganizationModel) event.getItemId();
		this.panel.setContent(model.getForm());
	}
	
	protected void onSave(ClickEvent event) {
		
	}
	
	protected void onReset(ClickEvent event) {
		
	}
	
	protected void onCreate(ClickEvent event) {
		
	}
}
