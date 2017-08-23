package za.co.yellowfire.threesixty.ui.component.container.kudos;

import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.ui.component.container.RepositoryContainer;
import za.co.yellowfire.threesixty.ui.component.container.RepositoryProvider;

import java.util.List;

@SuppressWarnings("serial")
public class BadgeContainer extends RepositoryContainer<Badge> {

	public BadgeContainer(final BadgeRepository repository) {
		super(new BadgeProvider(repository));
	}

	private static class BadgeProvider implements RepositoryProvider<Badge> {
		private final BadgeRepository repository;
		
		public BadgeProvider(final BadgeRepository repository) {
			this.repository = repository;
		}

		@Override
		public List<Badge> findEntities() {
			return this.repository.findByActive(true);
		}
	}
}
