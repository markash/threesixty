package za.co.yellowfire.threesixty.ui.view.rating;

import za.co.yellowfire.threesixty.domain.rating.Outcome;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class OutcomeEntityEditForm extends AbstractEntityEditForm<Outcome> {

	@Override
	protected Outcome buildEmpty() {
		return Outcome.EMPTY();
	}
}
