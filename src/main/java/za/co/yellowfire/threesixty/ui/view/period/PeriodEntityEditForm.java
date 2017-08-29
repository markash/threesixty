package za.co.yellowfire.threesixty.ui.view.period;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import io.threesixty.ui.component.card.CounterStatisticModel;
import io.threesixty.ui.component.card.CounterStatisticsCard;
import io.threesixty.ui.event.EnterEntityEditViewEvent;
import io.threesixty.ui.event.EventUtils;
import io.threesixty.ui.view.AbstractEntityEditForm;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.*;
import za.co.yellowfire.threesixty.ui.I8n;

import javax.annotation.PreDestroy;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings("serial")
public class PeriodEntityEditForm extends AbstractEntityEditForm<PeriodModel> implements EventBusListener<EnterEntityEditViewEvent<PeriodModel>> {

	private DateField startField = new DateField(I8n.Period.Fields.START);
	private DateField endField = new DateField(I8n.Period.Fields.END);
	private DateField publishDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_PUBLISH);
	private DateField completeDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_COMPLETE);
	private DateField selfDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_SELF_ASSESSMENT);
	private DateField assessorDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_ASSESSOR_ASSESSMENT);
	private CheckBox activeField = new CheckBox(I8n.Period.Fields.ACTIVE);

	private CounterStatisticsCard registeredAssessmentsCard;
	private CounterStatisticsCard publishedAssessmentsCard;
	private CounterStatisticsCard submittedAssessmentsCard;
	private CounterStatisticsCard reviewedAssessmentsCard;

	@SuppressWarnings("unused")
	private final PeriodService service;
	private final AssessmentService assessmentService;
	private final EventBus.SessionEventBus eventBus;

	private String[] nestedProperties = new String[] {
			Period.FIELD_DEADLINE_PUBLISH, 
			Period.FIELD_DEADLINE_COMPLETE, 
			Period.FIELD_DEADLINE_SELF_ASSESSMENT,
			Period.FIELD_DEADLINE_ASSESSOR_ASSESSMENT};
	
	PeriodEntityEditForm(
			final PeriodService periodService,
			final AssessmentService assessmentService,
            final EventBus.SessionEventBus eventBus) {

		super(PeriodModel.class);

		this.service = periodService;
		this.assessmentService = assessmentService;
		this.eventBus = eventBus;

		this.eventBus.subscribe(this);

		this.startField.setDateFormat(I8n.Format.DATE);
		this.endField.setDateFormat(I8n.Format.DATE);
		this.publishDeadlineField.setDateFormat(I8n.Format.DATE);
		this.completeDeadlineField.setDateFormat(I8n.Format.DATE);
		this.selfDeadlineField.setDateFormat(I8n.Format.DATE);
		this.assessorDeadlineField.setDateFormat(I8n.Format.DATE);

        getBinder().forField(startField).asRequired(I8n.Period.Validation.START_REQUIRED).bind(PeriodModel.FIELD_START);
        getBinder().forField(endField).asRequired(I8n.Period.Validation.END_REQUIRED).bind(PeriodModel.FIELD_END);
        getBinder().forField(publishDeadlineField).bind(PeriodModel.FIELD_DEADLINE_PUBLISHED);
        getBinder().forField(completeDeadlineField).bind(PeriodModel.FIELD_DEADLINE_COMPLETED);
        getBinder().forField(selfDeadlineField).bind(PeriodModel.FIELD_DEADLINE_SELF);
        getBinder().forField(assessorDeadlineField).bind(PeriodModel.FIELD_DEADLINE_MANAGER);
        getBinder().forField(activeField).bind(PeriodModel.FIELD_ACTIVE);

        startField.setRequiredIndicatorVisible(true);
        endField.setRequiredIndicatorVisible(true);

		registeredAssessmentsCard = new CounterStatisticsCard(
				"Registered",
				VaadinIcons.USERS,
				"The number of registered assessments for the review period.",
				"");

        publishedAssessmentsCard = new CounterStatisticsCard(
                "Published",
                VaadinIcons.USERS,
                "The number of published assessments for the review period.",
                "");

        submittedAssessmentsCard = new CounterStatisticsCard(
                "Submitted",
                VaadinIcons.USERS,
                "The number of submitted assessments for the review period.",
                "");

        reviewedAssessmentsCard = new CounterStatisticsCard(
                "Reviewed",
                VaadinIcons.USERS,
                "The number of completed assessments for the review period.",
                "");
	}
	
	/**
	 * Returns the list of nested properties that the form group should bind to
	 * @return An array of nested properties in Java object notation
	 */
	@Override
	protected String[] getNestedProperties() { return nestedProperties; }

	@Override
	protected void internalLayout() {

        MHorizontalLayout statsLine01 =
                new MHorizontalLayout(registeredAssessmentsCard, publishedAssessmentsCard)
                        .withSpacing(true);

        MHorizontalLayout statsLine02 =
                new MHorizontalLayout(submittedAssessmentsCard, reviewedAssessmentsCard)
                        .withSpacing(true);

		VerticalLayout statsPanel =
                new MVerticalLayout(
                        statsLine01,
                        statsLine02);

		Layout fieldsPanel = 
				new MHorizontalLayout(
                        new MFormLayout(startField, endField, publishDeadlineField, activeField),
					    new MFormLayout(selfDeadlineField, assessorDeadlineField, completeDeadlineField)
				);

		addComponents(fieldsPanel, statsPanel);
	}


    @Override
    public void onEvent(final org.vaadin.spring.events.Event<EnterEntityEditViewEvent<PeriodModel>> event) {

        Consumer<Period> refreshStatisticsCards = (p) -> {
            Map<AssessmentStatus, AssessmentStatusCount> statusCounts = assessmentService.countAssessmentsStatusFor(p);
            statusCounts.entrySet().forEach(e -> {

                Supplier<CounterStatisticModel> suppler = () -> new CounterStatisticModel("assessments", e.getValue().getCount());
                switch (e.getKey()) {
                    case All:
                        this.registeredAssessmentsCard.setStatisticSupplier(suppler);
                        break;
                    case Created:
                        this.publishedAssessmentsCard.setStatisticSupplier(suppler);
                        break;
                    case EmployeeCompleted:
                        this.submittedAssessmentsCard.setStatisticSupplier(suppler);
                        break;
                    case Reviewed:
                        this.reviewedAssessmentsCard.setStatisticSupplier(suppler);
                        break;
                }
            });
        };

        if (EventUtils.eventFor(event, PeriodEditView.class)) {
            final Optional<Period> period = Optional.ofNullable(event.getPayload().getEntity().getWrapped());
            period.ifPresent(refreshStatisticsCards);
        }
    }

    @PreDestroy
	@SuppressWarnings("unused")
    void destroy() {
        eventBus.unsubscribe(this);
    }
}
