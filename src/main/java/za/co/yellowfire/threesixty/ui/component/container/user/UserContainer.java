package za.co.yellowfire.threesixty.ui.component.container.user;

import java.util.List;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.component.container.RepositoryContainer;
import za.co.yellowfire.threesixty.ui.component.container.RepositoryProvider;

@SuppressWarnings("serial")
public class UserContainer extends RepositoryContainer<User> {

	public UserContainer(final UserService userService) {
		this(userService, false);
	}
	
	public UserContainer(final UserService userService, final boolean excludeCurrentUser) {
		super(new UserProvider(userService, excludeCurrentUser));
	}

	private static class UserProvider implements RepositoryProvider<User> {
		private boolean excludeCurrentUser;
		private final UserService userService;
		
		public UserProvider(final UserService userService, final boolean excludeCurrentUser) {
			this.userService = userService;
			this.excludeCurrentUser = excludeCurrentUser;
		}

		@Override
		public List<User> findEntities() {
			if (excludeCurrentUser) {
				return this.userService.findUsersExcept(userService.getCurrentUser());
			}
			return this.userService.findUsers();
		}
	}
}
