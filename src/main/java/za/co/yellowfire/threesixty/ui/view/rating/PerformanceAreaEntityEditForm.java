package za.co.yellowfire.threesixty.ui.view.rating;

import com.github.markash.ui.security.CurrentUserProvider;
import com.github.markash.ui.view.AbstractEntityEditForm;
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.domain.rating.PerformanceAreaRepository;
import za.co.yellowfire.threesixty.domain.user.User;

@SuppressWarnings("serial")
public class PerformanceAreaEntityEditForm extends AbstractEntityEditForm<PerformanceArea> {

	public PerformanceAreaEntityEditForm(
			final PerformanceAreaRepository performanceAreaRepository,
            final CurrentUserProvider<User> currentUserProvider) {

		super(PerformanceArea.class);
	}
}
