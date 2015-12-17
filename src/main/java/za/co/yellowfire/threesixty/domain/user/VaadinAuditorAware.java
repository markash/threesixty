package za.co.yellowfire.threesixty.domain.user;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import com.vaadin.server.VaadinSession;

@Service
public class VaadinAuditorAware implements AuditorAware<User>{
	private final Logger LOG = LoggerFactory.getLogger(VaadinAuditorAware.class);
	
	@Autowired
	private UserService userService;
	
	@Override
	public User getCurrentAuditor() {
		VaadinSession session = VaadinSession.getCurrent();
		
		/* When there is a session get the logged on user */
		if (session != null) {
			return (User) session.getAttribute(User.class.getName());
		}
		/* Return the administrator user since the server is performing the operation */
		try {
			return userService.findUser("admin");
		} catch (IOException e) {
			LOG.error("Unable to read user." , e.getMessage());
		}
		return null;
	}
}
