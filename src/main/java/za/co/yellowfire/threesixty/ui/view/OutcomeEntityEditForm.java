package za.co.yellowfire.threesixty.ui.view;

import za.co.yellowfire.threesixty.domain.question.Outcome;

@SuppressWarnings("serial")
public class OutcomeEntityEditForm extends AbstractEntityEditForm<Outcome> {

	@Override
	protected Outcome buildEmpty() {
		return Outcome.EMPTY();
	}
}
