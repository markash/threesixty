package za.co.yellowfire.threesixty.ui.view.rating;

import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class PerformanceAreaEntityEditForm extends AbstractEntityEditForm<PerformanceArea> {

	@Override
	protected PerformanceArea buildEmpty() {
		return PerformanceArea.EMPTY();
	}
}
