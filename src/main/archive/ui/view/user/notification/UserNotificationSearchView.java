package za.co.yellowfire.threesixty.ui.view.user.notification;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotificationRepository;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = UserNotificationSearchView.VIEW_NAME)
public final class UserNotificationSearchView extends AbstractTableSearchView<UserNotification, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Notifications.Views.Search.TITLE;
	public static final String VIEW_NAME = "notifications";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String[] TABLE_PROPERTIES = {"id", "category", "action", "time", "user"};
    public static final String[] TABLE_FILTERS = {"id", "category", "action", "time", "user"};
    public static final String[] TABLE_HEADERS = {"Id", "Category", "Action", "Time", "User"};
    
    protected Button newButton = ButtonBuilder.NEW(this::onCreate);
    protected Button[] tableButtons = {newButton};
    
    @Autowired
    public UserNotificationSearchView(UserNotificationRepository repository) {
    	super(UserNotification.class, new SpringEntityProvider<UserNotification>(repository), TABLE_FILTERS);
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
			tableButtons = new Button[] { ButtonBuilder.NEW(this::onCreate) };
		}
		return this.tableButtons; 
	}
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(UserNotificationEditView.VIEW_NEW());
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(UserNotificationEditView.VIEW(value));
	}
	
	@Override
	protected UserNotification buildEmpty() {
		return null;
	}
}

