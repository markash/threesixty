package za.co.yellowfire.threesixty.ui.view.period;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.spring.annotation.SpringView;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.events.EventBus;

import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

@Secured("ROLE_ADMIN")
@SpringView(name = PeriodEditView.VIEW_NAME)
public class PeriodEditView extends AbstractEntityEditView<PeriodModel> {
	private static final long serialVersionUID = 1L;

	private EventBus.SessionEventBus eventBus;

	public static final String TITLE = I8n.Period.SINGULAR;
	public static final String VIEW_NAME = "period";

    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }

    @Autowired
    public PeriodEditView(
			final EntitySupplier<PeriodModel, Serializable> periodSupplier,
    		final BlankSupplier<PeriodModel> blankPeriodSupplier,
			final EntityPersistFunction<PeriodModel> periodPersistFunction,
    		final PeriodEntityEditForm periodEntityEditForm,
			final EventBus.SessionEventBus eventBus) {
    	
    	super(TITLE,
                periodEntityEditForm,
                periodSupplier,
                blankPeriodSupplier,
                periodPersistFunction);
    	this.eventBus = eventBus;
    }

	@Override
    protected void publishOnEventBus(final ApplicationEvent event) {
        Optional.ofNullable(eventBus).ifPresent(eb -> eb.publish(this, event));
    }
}

