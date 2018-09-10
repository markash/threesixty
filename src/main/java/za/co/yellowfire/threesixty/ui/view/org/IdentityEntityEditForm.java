package za.co.yellowfire.threesixty.ui.view.org;

import com.github.markash.ui.component.EntityGrid;
import com.github.markash.ui.component.panel.PanelBuilder;
import com.github.markash.ui.view.AbstractEntityEditForm;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.organization.Identity;
import za.co.yellowfire.threesixty.domain.organization.IdentityType;
import za.co.yellowfire.threesixty.domain.organization.OrganizationLevelMetadata;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;

public class IdentityEntityEditForm extends AbstractEntityEditForm<Identity> {
	private static final long serialVersionUID = 1L;

	private MTextField nameField =
		new MTextField(I8n.Identity.Fields.NAME)
			.withFullWidth();

	private ComboBox<IdentityType> typeField;
	
	private UserService userService;
	private OrganizationIconResolver iconResolver;
	private EntityGrid<User> userTable;
	private String[] organisationLevelPropertyNames = {"name", "type", "icon"};
	private String[] organisationLevelHeaderNames = {"Name", "Type", "Icon"};
	private String[] userPropertyNames = {"id", "fullName", "position"};
	private String[] userHeaderNames = {"#", "Name", "Position"};
	
	public IdentityEntityEditForm(
			final UserService userService, 
			final OrganizationIconResolver iconResolver) {

		super(Identity.class);

		this.userService = userService;
		this.iconResolver = iconResolver;
		this.typeField = 
				new ComboBox<>(
						I8n.Identity.Fields.TYPE,
						IdentityType.getValues());
		this.typeField.setWidth(100.0f, Unit.PERCENTAGE);

		this.userTable = new EntityGrid<>(User.class)
				.withDefinition(userTableDefinition())
				.withDataProvider(new UserDataProvider(userService.getUserRepository()));
		this.userTable.setWidth(100.0f, Unit.PERCENTAGE);

        getBinder().bind(nameField, "name");
        getBinder().bind(typeField, "type");
	}

	@Override
	protected void internalLayout() {
		
		OrganizationModelType modelType = OrganizationModelType.CHILD;
		
		if (getValue().getMetadata().isPresent()) {
			OrganizationLevelMetadata metadata = getValue().getMetadata().get();
			if (!metadata.hasParent()) {
				modelType = OrganizationModelType.ROOT;
				//this.table = buildOrganizationLevelTable(metadata.getIdentityMetadata().getLevels());
			} else {
				//this.table = buildUserTable(userService.findUsersForDepartment(getValue()));
			}
		}

		MVerticalLayout layout = new MVerticalLayout()
				.withMargin(false)
				.withWidth(100.0f, Unit.PERCENTAGE)
				.with(nameField, typeField, new Label(modelType.display),  this.userTable);
		addComponent(layout);
	}
	
//	private Grid<UserModel> buildUserTable(final List<User> values) {
//
//		List<UserModel> list = new ArrayList<>();
//		values.forEach(user -> list.add(new UserModel(user)));
//
//		UserEntityProvider entityProvider = new UserEntityProvider(list);
//		Grid<UserModel> table = new Grid<>(UserModel.class)
//				.setBeans(new SortableLazyList<UserModel>(entityProvider, entityProvider, 100))
//                .withProperties(userPropertyNames)
//                .withColumnHeaders(userHeaderNames)
//                ;
//
//		table.setWidth(Style.Percentage._100);
//
//		return table;
//	}

	private TableDefinition<User> userTableDefinition() {

		TableDefinition<User> tableDefinition = new TableDefinition<>(IdentityView.VIEW_NAME);
		tableDefinition
				.column(String.class)
				.withHeading(I8n.User.Columns.ID)
				.forProperty(User.FIELD_ID)
				.enableTextSearch()
				.identity();
		tableDefinition
				.column(String.class)
				.withHeading(I8n.User.Columns.FIRST_NAME)
				.forProperty(User.FIELD_FIRST_NAME)
				.enableTextSearch();
		tableDefinition
				.column(String.class)
				.withHeading(I8n.User.Columns.LAST_NAME)
				.forProperty(User.FIELD_LAST_NAME)
				.enableTextSearch();
		tableDefinition
				.column(String.class)
				.withHeading(I8n.User.Columns.ROLE)
				.forProperty(User.FIELD_ROLE);
		tableDefinition
				.column(String.class)
				.withHeading(I8n.User.Columns.EMAIL)
				.forProperty(User.FIELD_EMAIL)
				.enableTextSearch();

		return tableDefinition;
	}

//	private Grid<OrganizationLevelMetadataModel> buildOrganizationLevelTable(
//			final List<OrganizationLevelMetadata> values) {
//
//		List<OrganizationLevelMetadataModel> list = new ArrayList<>();
//		values.forEach(metadata -> list.add(new OrganizationLevelMetadataModel(metadata, iconResolver)));
//
//		OrganizationLevelEntityProvider entityProvider = new OrganizationLevelEntityProvider(list);
//		MGrid<OrganizationLevelMetadataModel> table = new MGrid<>(OrganizationLevelMetadataModel.class)
//				.setRows(list)
//                .withProperties(organisationLevelPropertyNames)
//                .withColumnHeaders(organisationLevelHeaderNames)
//                ;
//
//		table.withWidth(Style.Percentage._100);
//
//		table.addGeneratedColumn(
//				organisationLevelPropertyNames[2],
//				(Table source, Object itemId, Object columnId) -> {
//					Item x = source.getItem(itemId);
//					@SuppressWarnings("unchecked")
//					Property<FontAwesome> icon = x.getItemProperty(organisationLevelPropertyNames[2]);
//					return new Label(icon.getValue().getHtml(), ContentMode.HTML);
//				});
//
//		return table;
//	}
	
	protected static enum OrganizationModelType {
		ROOT("Identity Levels") ,
		CHILD("Users");
		
		private String display;
		OrganizationModelType(final String display) { this.display = display; }
		public String getDisplay() { return this.display; }
	}
//
//	@SuppressWarnings("serial")
//	protected static class OrganizationLevelEntityProvider implements SortableLazyList.SortableEntityProvider<OrganizationLevelMetadataModel> {
//
//		private List<OrganizationLevelMetadataModel> values;
//		public OrganizationLevelEntityProvider(final List<OrganizationLevelMetadataModel> values) { this.values = values; }
//		@Override
//		public List<OrganizationLevelMetadataModel> findEntities(int firstRow, boolean sortAscending, String property) { return values; }
//		@Override
//		public int size() { return this.values.size(); }
//	}
//
//	@SuppressWarnings("serial")
//	protected static class UserEntityProvider implements SortableLazyList.SortableEntityProvider<UserModel> {
//
//		private List<UserModel> values;
//		public UserEntityProvider(final List<UserModel> values) { this.values = values; }
//		@Override
//		public List<UserModel> findEntities(int firstRow, boolean sortAscending, String property) { return values; }
//		@Override
//		public int size() { return this.values.size(); }
//	}
//
//	public static class OrganizationLevelMetadataModel {
//		private final OrganizationLevelMetadata metadata;
//		private final OrganizationIconResolver iconResolver;
//
//		public OrganizationLevelMetadataModel(final OrganizationLevelMetadata metadata, final OrganizationIconResolver iconResolver) {
//			this.metadata = metadata;
//			this.iconResolver = iconResolver;
//		}
//		public String getName() { return metadata.getName(); }
//		public IdentityType getType() { return metadata.getType(); }
//		public FontAwesome getIcon() { return iconResolver.getIcon(this.metadata); }
//	}
//
//	public static class UserModel {
//		private final User user;
//
//		public UserModel(final User user) {
//			this.user = user;
//		}
//		public String getId() { return user.getId(); }
//		public String getFullName() { return user.getFullName(); }
//		public Position getPosition() { return user.getPosition(); }
//	}
}
