package za.co.yellowfire.threesixty.ui.view.org;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.FilterableTable;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import za.co.yellowfire.threesixty.domain.organization.Organization;
import za.co.yellowfire.threesixty.domain.organization.OrganizationLevelMetadata;
import za.co.yellowfire.threesixty.domain.organization.OrganizationType;
import za.co.yellowfire.threesixty.domain.user.Position;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

import java.util.ArrayList;
import java.util.List;

public class OrganizationEntityEditForm extends AbstractEntityEditForm<Organization> {
	private static final long serialVersionUID = 1L;

	private MTextField nameField =
		new MTextField(I8n.Organization.Fields.NAME)
			.withFullWidth();

	private ComboBox<OrganizationType> typeField;
	
	private UserService userService;
	private OrganizationIconResolver iconResolver;
	private Grid<UserModel> table;
	private String[] organisationLevelPropertyNames = {"name", "type", "icon"};
	private String[] organisationLevelHeaderNames = {"Name", "Type", "Icon"};
	private String[] userPropertyNames = {"id", "fullName", "position"};
	private String[] userHeaderNames = {"#", "Name", "Position"};
	
	public OrganizationEntityEditForm(
			final UserService userService, 
			final OrganizationIconResolver iconResolver) {

		super(Organization.class);

		this.userService = userService;
		this.iconResolver = iconResolver;
		this.typeField = 
				new ComboBox<>(
						I8n.Organization.Fields.TYPE, 
						OrganizationType.getValues());
		this.typeField.setWidth(Style.Percentage._100);

        getBinder().bind(nameField, "name");
        getBinder().bind(typeField, "type");
	}

	@Override
	protected Organization buildEmpty() {
		return new Organization();
	}

	@Override
	protected void internalLayout() {
		
		OrganizationModelType modelType = OrganizationModelType.CHILD;
		
		if (getValue().getMetadata().isPresent()) {
			OrganizationLevelMetadata metadata = getValue().getMetadata().get();
			if (!metadata.hasParent()) {
				modelType = OrganizationModelType.ROOT;
				this.table = buildOrganizationLevelTable(metadata.getOrganizationMetadata().getLevels());
			} else {
				this.table = buildUserTable(userService.findUsersForDepartment(getValue()));
			}
		}
		
		addComponent(PanelBuilder.FORM(nameField, typeField, new Label(modelType.display),  this.table));
	}
	
	private Grid<UserModel> buildUserTable(final List<User> values) {
		
		List<UserModel> list = new ArrayList<>();
		values.forEach(user -> list.add(new UserModel(user)));
	
		UserEntityProvider entityProvider = new UserEntityProvider(list);
		Grid<UserModel> table = new Grid<>(UserModel.class)
				.setBeans(new SortableLazyList<UserModel>(entityProvider, entityProvider, 100))
                .withProperties(userPropertyNames)
                .withColumnHeaders(userHeaderNames)
                ;
		
		table.setWidth(Style.Percentage._100);
		
		return table;
	}
	
	private MTable<OrganizationLevelMetadataModel> buildOrganizationLevelTable(final List<OrganizationLevelMetadata> values) {
		
		List<OrganizationLevelMetadataModel> list = new ArrayList<>();
		values.forEach(metadata -> list.add(new OrganizationLevelMetadataModel(metadata, iconResolver)));
		
		OrganizationLevelEntityProvider entityProvider = new OrganizationLevelEntityProvider(list);
		MTable<OrganizationLevelMetadataModel> table = new FilterableTable<OrganizationLevelMetadataModel>(OrganizationLevelMetadataModel.class)
				.setBeans(new SortableLazyList<OrganizationLevelMetadataModel>(entityProvider, entityProvider, 100))
                .withProperties(organisationLevelPropertyNames)
                .withColumnHeaders(organisationLevelHeaderNames)
                ;
		
		table.withWidth(Style.Percentage._100);
		
		table.addGeneratedColumn(
				organisationLevelPropertyNames[2], 
				(Table source, Object itemId, Object columnId) -> {
					Item x = source.getItem(itemId);			
					@SuppressWarnings("unchecked")
					Property<FontAwesome> icon = x.getItemProperty(organisationLevelPropertyNames[2]);				
					return new Label(icon.getValue().getHtml(), ContentMode.HTML);
				});
		
		return table;
	}
	
	protected static enum OrganizationModelType {
		ROOT("Organization Levels") ,
		CHILD("Users");
		
		private String display;
		OrganizationModelType(final String display) { this.display = display; }
		public String getDisplay() { return this.display; }
	}
	
	@SuppressWarnings("serial")
	protected static class OrganizationLevelEntityProvider implements SortableLazyList.SortableEntityProvider<OrganizationLevelMetadataModel> {

		private List<OrganizationLevelMetadataModel> values;
		public OrganizationLevelEntityProvider(final List<OrganizationLevelMetadataModel> values) { this.values = values; }
		@Override
		public List<OrganizationLevelMetadataModel> findEntities(int firstRow, boolean sortAscending, String property) { return values; }
		@Override
		public int size() { return this.values.size(); }
	}
	
	@SuppressWarnings("serial")
	protected static class UserEntityProvider implements SortableLazyList.SortableEntityProvider<UserModel> {

		private List<UserModel> values;
		public UserEntityProvider(final List<UserModel> values) { this.values = values; }
		@Override
		public List<UserModel> findEntities(int firstRow, boolean sortAscending, String property) { return values; }
		@Override
		public int size() { return this.values.size(); }
	}
	
	public static class OrganizationLevelMetadataModel {
		private final OrganizationLevelMetadata metadata;
		private final OrganizationIconResolver iconResolver;
		
		public OrganizationLevelMetadataModel(final OrganizationLevelMetadata metadata, final OrganizationIconResolver iconResolver) {
			this.metadata = metadata;
			this.iconResolver = iconResolver;
		}
		public String getName() { return metadata.getName(); }
		public OrganizationType getType() { return metadata.getType(); }
		public FontAwesome getIcon() { return iconResolver.getIcon(this.metadata); }
	}
	
	public static class UserModel {
		private final User user;
		
		public UserModel(final User user) {
			this.user = user;
		}
		public String getId() { return user.getId(); }
		public String getFullName() { return user.getFullName(); }
		public Position getPosition() { return user.getPosition(); }
	}
}
