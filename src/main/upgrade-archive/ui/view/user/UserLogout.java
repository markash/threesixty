package za.co.yellowfire.threesixty.ui.view.user;

import com.github.markash.ui.event.LogoutEvent;
import com.vaadin.icons.VaadinIcons;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.ui.I8n;

@Component
@SuppressWarnings("unused")
@VaadinFontIcon(VaadinIcons.LOCK)
@SideBarItem(sectionId = Sections.PROFILE, caption = UserLogout.TITLE)
public class UserLogout implements Runnable {

    public static final String TITLE = I8n.User.LOGOUT;

    private final ApplicationEventPublisher publisher;

    public UserLogout(
            final ApplicationEventPublisher publisher) {

        this.publisher = publisher;
    }

    @Override
    public void run() {
        this.publisher.publishEvent(new LogoutEvent(this));
    }
}
