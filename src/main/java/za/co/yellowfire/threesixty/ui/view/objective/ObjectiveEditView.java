package za.co.yellowfire.threesixty.ui.view.objective;

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
import za.co.yellowfire.threesixty.domain.rating.Objective;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;
import java.util.Optional;

@Secured("ROLE_ADMIN")
@SpringView(name = ObjectiveEditView.VIEW_NAME)
public class ObjectiveEditView extends AbstractEntityEditView<Objective> {
	private static final long serialVersionUID = 1L;

    public static final String TITLE = I8n.Objective.SINGULAR;
    public static final String VIEW_NAME = "outcome";

    private EventBus.SessionEventBus eventBus;

    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }

    @Autowired
    public ObjectiveEditView(
            final EntitySupplier<Objective, Serializable> objectiveSupplier,
            final BlankSupplier<Objective> blankObjectiveSupplier,
            final EntityPersistFunction<Objective> objectivePersistFunction,
            final ObjectiveEntityEditForm objectiveEntityEditForm,
            final EventBus.SessionEventBus eventBus) {
    	super(TITLE, objectiveEntityEditForm, objectiveSupplier, blankObjectiveSupplier, objectivePersistFunction);

    	this.eventBus = eventBus;
    }

    @Override
    protected void publishOnEventBus(final ApplicationEvent event) {
        Optional.ofNullable(eventBus).ifPresent(eb -> eb.publish(this, event));
    }
}

