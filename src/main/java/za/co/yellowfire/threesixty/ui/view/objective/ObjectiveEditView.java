package za.co.yellowfire.threesixty.ui.view.objective;

import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.spring.annotation.SpringView;
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
@SpringView(name = ObjectiveEditView.VIEW_NAME)
public class ObjectiveEditView extends AbstractEntityEditView<String, Objective> {
	private static final long serialVersionUID = 1L;

    public static final String TITLE = I8n.Objective.SINGULAR;
    public static final String VIEW_NAME = I8n.Objective.EDIT_VIEW;

    private EventBus.SessionEventBus eventBus;

    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }

    @Autowired
    public ObjectiveEditView(
            final ObjectiveService objectiveService,
            final EventBus.SessionEventBus eventBus,
            final ObjectiveEntityEditForm objectiveEntityEditForm) {

    	super(TITLE, objectiveEntityEditForm);

        setEntitySupplier(id -> Optional.ofNullable(objectiveService.findById((String) id)));

        setBlankSupplier(Objective::new);

        setEntityPersistFunction(objective -> {
            try {
                return objectiveService.save(objective);
            } catch (Exception e) {
                NotificationBuilder.showNotification("Persist", e.getMessage());
            }
            return objective;
        });

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

