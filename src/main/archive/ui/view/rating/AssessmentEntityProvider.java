package za.co.yellowfire.threesixty.ui.view.rating;

import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;

public class AssessmentEntityProvider extends SpringEntityProvider<Assessment> {
	private static final long serialVersionUID = 1L;
	
	private User currentUser;
	
	public AssessmentEntityProvider(AssessmentRepository repository, final User currentUser) {
		super(repository);
		this.currentUser = currentUser;
	}
	
	@Override
	protected Iterable<Assessment> getResults() {
		if (currentUser.isAdmin()) {
			return ((AssessmentRepository) getRepository()).findAll();
		}
		return ((AssessmentRepository) getRepository()).findByAccessTo(currentUser.getId());
	}
}
