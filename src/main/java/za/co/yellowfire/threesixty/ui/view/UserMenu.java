package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.DashboardEvent.UserLogoutEvent;
import za.co.yellowfire.threesixty.ui.DashboardEventBus;
import za.co.yellowfire.threesixty.ui.component.ByteArrayStreamResource;

@SuppressWarnings("serial")
public class UserMenu extends MenuBar {

	private MenuItem profileItem;
	
	public UserMenu(final User user) {
        this.addStyleName("user-menu");
        internalCreateUpdateProfile(user);
	}
	
	public void updateUser(final User user) {
		internalCreateUpdateProfile(user);
	}
	
	private void internalCreateUpdateProfile(final User user) {
		
		Resource resource = null;
		if (user != null && user.hasPicture()) {
			resource = new ByteArrayStreamResource(user.getPictureContent(), user.getPictureName());
		} else {
			resource = new ThemeResource("img/profile-pic-300px.jpg");
		}
		
		String profile;
        if ((user.getFirstName() == null || user.getLastName() == null) && user.isAdmin()) {
        	profile = "Administrator";
        } else {
        	profile = user.getFirstName() + " " + user.getLastName();
        }
		
		if (profileItem == null) {
			profileItem = this.addItem(profile, resource, null);
			profileItem.addItem("Edit Profile", new NavigateToProfileCommand());
	        profileItem.addItem("Preferences", new NavigateToProfileCommand());
	        profileItem.addSeparator();
	        profileItem.addItem("Sign Out", new SignoutCommand());
		} else {
			profileItem.setIcon(resource);
			profileItem.setText(profile);
		}
	}
	
	public class NavigateToProfileCommand implements Command {
		@Override
		public void menuSelected(MenuItem selectedItem) {
			UI.getCurrent().getNavigator().navigateTo(UserEditView.VIEW(getCurrentUser().getId()));
		}
    }
    
	public class SignoutCommand implements Command {
		@Override
		public void menuSelected(MenuItem selectedItem) {
			DashboardEventBus.post(new UserLogoutEvent());
		}
    }
    
    public User getCurrentUser() {
    	return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
    }
}
