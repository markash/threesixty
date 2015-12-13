package za.co.yellowfire.threesixty.ui.view;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;

import za.co.yellowfire.threesixty.domain.question.Questionaire;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserRepository;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;

@SpringView(name = UserSearchView.VIEW_NAME)
public final class UserSearchView extends AbstractTableSearchView<User, String> /*, DashboardEditListener*/ {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Users";
	public static final String EDIT_ID = "user-edit";
    public static final String TITLE_ID = "user-title";
    public static final String VIEW_NAME = "users";
    public static final String[] TABLE_PROPERTIES = {"id", "firstName", "lastName", "role", "email",  "website"};
    public static final String[] TABLE_FILTERS = {"id", "firstName", "lastName", "role", "email",  "website"};
    public static final String[] TABLE_HEADERS = {"#", "Name", "Surname", "Role", "Email", "Website"};
    
    protected Button[] tableButtons;
    
    @Autowired
    public UserSearchView(UserRepository repository) {
    	super(User.class, new SpringEntityProvider<User>(repository), TABLE_FILTERS);
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }

	protected String[] getTablePropertyNames() { return TABLE_PROPERTIES; }
	protected String[] getTablePropertyHeaders() { return TABLE_HEADERS; }
	
	protected Button[] getTableButtons() { 
		if (this.tableButtons == null) {
			this.tableButtons = new Button[] { buildCreateButton() };
		}
		return this.tableButtons; 
	}
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(UserEditView.VIEW_NAME + "/new-user");
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(UserEditView.VIEW_NAME + "/" + value);
	}
	
	private Button buildCreateButton() {
        final Button button = new Button("New");
        button.setIcon(FontAwesome.PENCIL);
        button.setDescription("Create a new user");
        button.addClickListener(event -> { onCreate(event); });
        return button;
    }
	
	@Override
	protected User buildEmpty() {
		return null;
	}
}

