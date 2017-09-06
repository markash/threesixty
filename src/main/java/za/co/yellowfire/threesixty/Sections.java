package za.co.yellowfire.threesixty;

import org.springframework.stereotype.Component;
import org.vaadin.spring.sidebar.annotation.SideBarSection;
import org.vaadin.spring.sidebar.annotation.SideBarSections;

@SideBarSections({
        @SideBarSection(id = Sections.DEFAULT, caption = "", order=1),
        @SideBarSection(id = Sections.DASHBOARD, caption = "Dashboard", order=2),
        @SideBarSection(id = Sections.PROFILE, caption = "Profile", order=30)
})
@Component
public class Sections {
	public static final String DEFAULT = "default";
	public static final String DASHBOARD = "dashboard";
	public static final String PROFILE = "profile";
}
