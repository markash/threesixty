package za.co.yellowfire.threesixty.domain.user;

import io.threesixty.ui.security.DefaultUserPrincipal;

public class UserPrincipal extends DefaultUserPrincipal<User> {

    private UserPrincipal(final User user) {
        super(user);
    }

    static  io.threesixty.ui.security.UserPrincipal wrap(final User user) {
        return new UserPrincipal(user);
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return getUser().getId();
    }
}
