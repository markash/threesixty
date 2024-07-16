package za.co.yellowfire.threesixty.ui.view.notification;

import java.io.Serializable;

public class UserNotificationFilter implements Serializable {

    private String userName;

    public UserNotificationFilter(
            final String userName) {

        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
