package za.co.yellowfire.threesixty.domain.user;

import com.vaadin.server.VaadinSession;

/**
 * Gets the current user from the Vaadin session
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
public class SessionCurrentUserProvider implements CurrentUserProvider {
    public User get() {
        return VaadinSession.getCurrent().getAttribute(User.class);
    }
}
