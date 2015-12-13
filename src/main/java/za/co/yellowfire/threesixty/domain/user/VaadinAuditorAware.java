package za.co.yellowfire.threesixty.domain.user;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinSession;

@Service
public class VaadinAuditorAware implements AuditorAware<User>{

	@Override
	public User getCurrentAuditor() {
		return (User) VaadinSession.getCurrent().getAttribute(User.class.getName());
	}
}
