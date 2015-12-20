package za.co.yellowfire.threesixty.ui.view.user.notification;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.domain.user.notification.UserNotification;
import za.co.yellowfire.threesixty.domain.user.notification.UserNotificationRepository;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.AbstractRepositoryEntityEditView;

@SpringView(name = UserNotificationEditView.VIEW_NAME)
public final class UserNotificationEditView extends AbstractRepositoryEntityEditView<UserNotification, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Notifications.Views.Edit.TITLE;
	public static final String VIEW_NAME = "notification";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String VIEW(final String entity) { return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity); } 
    public static final String VIEW_NEW() { return VIEW("new_" + VIEW_NAME); }
    
    @Autowired
    public UserNotificationEditView(final UserNotificationRepository repository, final UserNotificationEntityEditForm form) {
    	super(repository, form);
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(UserNotificationEditView.VIEW_NEW());
	}
}

