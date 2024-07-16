package za.co.yellowfire.threesixty.ui.view.objective;

import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.events.EventBus;
import za.co.yellowfire.threesixty.domain.rating.Objective;
import za.co.yellowfire.threesixty.domain.rating.ObjectiveService;
import za.co.yellowfire.threesixty.ui.I8n;

import java.util.Optional;

@Secured("ROLE_ADMIN")
@Route(value = ObjectiveEditView.VIEW_NAME)
public class ObjectiveEditView extends AbstractEntityEditView<Objective> {

    public static final String TITLE = I8n.Objective.SINGULAR;
    public static final String VIEW_NAME = I8n.Objective.EDIT_VIEW;

    private final EventBus.SessionEventBus eventBus;

    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }

    @Autowired
    public ObjectiveEditView(
            final ObjectiveService objectiveService,
            final EventBus.SessionEventBus eventBus,
            final ObjectiveEntityEditForm objectiveEntityEditForm) {

    	super(
                TITLE,
                objectiveEntityEditForm,
                id -> objectiveService.findById((String) id),
                Objective::new,
			    objectiveService::save);

    	this.eventBus = eventBus;
    }

    @Override
    protected void publishOnEventBus(
            final ApplicationEvent event) {

        Optional.ofNullable(eventBus).ifPresent(eb -> eb.publish(this, event));
    }

    @Override
    protected String successfulPersistNotification(
            final Objective entity) {

        return entity.getName() + " successfully persisted.";
    }
}

