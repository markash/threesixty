package za.co.yellowfire.threesixty.ui.view.rating;

import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodRepository;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;

public class PeriodEntityProvider extends SpringEntityProvider<Period> {
	private static final long serialVersionUID = 1L;
	
	public PeriodEntityProvider(PeriodRepository repository) {
		super(repository);
	}
}
