package za.co.yellowfire.threesixty.domain.user;

import com.vaadin.flow.server.VaadinSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VaadinAuditorAware /*implements AuditorAware<User>*/ {
	private final Logger LOG = LoggerFactory.getLogger(VaadinAuditorAware.class);
	
//	@Autowired
//	private UserRepository userRepository;

//	@Override
//	public Optional<User> getCurrentAuditor() {
//		VaadinSession session = VaadinSession.getCurrent();
//
//		/* When there is a session get the logged on user */
//		if (session != null) {
//			return Optional.ofNullable((User) session.getAttribute(User.class.getName()));
//		}
//		/* Return the administrator user since the server is performing the operation */
//		return userRepository.findOne(Example.of(User.ID("admin")));
//	}
}
