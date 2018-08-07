package za.co.yellowfire.threesixty.domain.user;

import com.vaadin.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

@Service
public class VaadinAuditorAware implements AuditorAware<User>{
	private final Logger LOG = LoggerFactory.getLogger(VaadinAuditorAware.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public User getCurrentAuditor() {
		VaadinSession session = VaadinSession.getCurrent();
		
		/* When there is a session get the logged on user */
		if (session != null) {
			return (User) session.getAttribute(User.class.getName());
		}
		/* Return the administrator user since the server is performing the operation */
		return userRepository.findOne("admin");
	}
}
