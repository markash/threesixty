package za.co.yellowfire.threesixty.ui.view.objective;

import io.threesixty.ui.event.EnterEntityEditViewEvent;
import io.threesixty.ui.view.AbstractEntityEditForm;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import za.co.yellowfire.threesixty.domain.rating.Outcome;
import za.co.yellowfire.threesixty.ui.view.period.PeriodModel;

import javax.annotation.PreDestroy;

@SuppressWarnings("serial")
public class ObjectiveEntityEditForm extends AbstractEntityEditForm<Outcome> implements EventBusListener<EnterEntityEditViewEvent<PeriodModel>>  {

	private final EventBus.SessionEventBus eventBus;

	ObjectiveEntityEditForm(
			final EventBus.SessionEventBus eventBus) {
		super(Outcome.class);

		this.eventBus = eventBus;
		this.eventBus.subscribe(this);
	}

	@Override
	public void onEvent(final org.vaadin.spring.events.Event<EnterEntityEditViewEvent<PeriodModel>> event) {
	}

	@PreDestroy
	@SuppressWarnings("unused")
	void destroy() {
		eventBus.unsubscribe(this);
	}
}
