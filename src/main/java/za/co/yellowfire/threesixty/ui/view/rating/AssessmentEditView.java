package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.UI;
import io.threesixty.ui.component.BlankSupplier;
import io.threesixty.ui.component.EntityPersistFunction;
import io.threesixty.ui.component.EntitySupplier;
import io.threesixty.ui.view.AbstractEntityEditView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.vaadin.spring.events.EventBus;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.period.PeriodModel;

import java.io.Serializable;
import java.util.Optional;

@SpringView(name = AssessmentEditView.VIEW_NAME)
public class AssessmentEditView extends AbstractEntityEditView<Assessment> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Assessment.SINGULAR;
	public static final String VIEW_NAME = "assessment";

	private EventBus.SessionEventBus eventBus;

    public static String VIEW(
			final String id,
			final PeriodModel period) {

    	return VIEW_NAME +
				((StringUtils.isBlank(id) ? "" : "/" + id) +
				"/" + period.getId()
				);
    }

    @Autowired
    public AssessmentEditView(
			final EntitySupplier<Assessment, Serializable> assessmentSupplier,
			final BlankSupplier<Assessment> blankAssessmentSupplier,
			final EntityPersistFunction<Assessment> assessmentPersistFunction,
			final AssessmentEntityEditForm assessmentEntityEditForm,
			final EventBus.SessionEventBus eventBus) {
    	
    	super(TITLE, assessmentEntityEditForm, assessmentSupplier, blankAssessmentSupplier, assessmentPersistFunction);
        this.eventBus = eventBus;
    }

	@Override
	protected void publishOnEventBus(final ApplicationEvent event) {
		Optional.ofNullable(eventBus).ifPresent(eb -> eb.publish(this, event));
	}
}

