package za.co.yellowfire.threesixty.ui.view.user;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import io.threesixty.ui.view.AbstractTableSearchView;
import io.threesixty.ui.view.TableDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;

@SuppressWarnings("unused")
@Secured("ROLE_ADMIN")
@VaadinFontIcon(VaadinIcons.USERS)
@SpringView(name = UserSearchView.VIEW_NAME)
@SideBarItem(sectionId = Sections.ADMINISTRATION, caption = UserSearchView.TITLE)
public class UserSearchView extends AbstractTableSearchView<User, String>  /*, DashboardEditListener*/ {
	private static final long serialVersionUID = 1L;

	public static final String TITLE = I8n.User.PLURAL;
    public static final String VIEW_NAME = "users";
	public static final VaadinIcons ICON = VaadinIcons.USERS;

    @Autowired
    public UserSearchView(
    		final ListDataProvider<User> userListDataProvider,
			final TableDefinition<User> userTableDefinition) {
    	super(User.class, TITLE, userListDataProvider, userTableDefinition);

		getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

	public void onCreate(ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(UserEditView.VIEW("/new-user")); }
}