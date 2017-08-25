package za.co.yellowfire.threesixty.ui.view.period;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import io.threesixty.ui.component.card.CounterStatisticModel;
import io.threesixty.ui.component.card.CounterStatisticsCard;
import io.threesixty.ui.event.EnterEntityEditViewEvent;
import io.threesixty.ui.view.AbstractEntityEditForm;
import org.springframework.context.ApplicationListener;
import org.vaadin.spring.events.Event;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.ui.I8n;

import javax.annotation.PreDestroy;
import java.util.Optional;

@SuppressWarnings("serial")
public class PeriodEntityEditForm extends AbstractEntityEditForm<PeriodModel> implements EventBusListener<EnterEntityEditViewEvent<PeriodModel>> {

	private DateField startField = new DateField(I8n.Period.Fields.START);
	private DateField endField = new DateField(I8n.Period.Fields.END);
	private DateField publishDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_PUBLISH);
	private DateField completeDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_COMPLETE);
	private DateField selfDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_SELF_ASSESSMENT);
	private DateField assessorDeadlineField = new DateField(I8n.Period.Fields.DEADLINE_ASSESSOR_ASSESSMENT);
	private CheckBox activeField = new CheckBox(I8n.Period.Fields.ACTIVE);

//	@PropertyId(Period.FIELD_REGISTERED_ASSESSMENTS)
//	private MStatsField registeredUsers = new MStatsField("0", "Registered users", "0% up from the previous week", I8n.User.ICON, MStatsField.STYLE_ERROR);
//	@PropertyId(Period.FIELD_PUBLISHED_ASSESSMENTS)
//	private MStatsField publishedAssessments = new MStatsField("0", "Published assessments", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_INFO);
//	@PropertyId(Period.FIELD_EMPLOYEE_ASSESSMENTS)
//	private MStatsField employeeAssessments = new MStatsField("0", "Self-rating assessments completed", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_WARNING);
//	@PropertyId(Period.FIELD_COMPLETED_ASSESSMENTS)
//	private MStatsField completedAssessments = new MStatsField("0", "Completed assessments", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_SUCCESS);

	private CounterStatisticsCard registeredAssessmentsCard;

	@SuppressWarnings("unused")
	private final PeriodService service;
	private final AssessmentService assessmentService;
	private final EventBus.SessionEventBus eventBus;
//	private final MStatsConverter<AssessmentStatusCount> assessmentCountConverter = new AssessmentStatusCountStatusConverter();
	
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
				"Registered users",
				VaadinIcons.USERS,
				"The number of registered assessments for the review period.",
				"");

	}
	
	/**
	 * Returns the list of nested properties that the form group should bind to
	 * @returns An array of nested properties in Java object notation
	 */
	@Override
	protected String[] getNestedProperties() { return nestedProperties; }

	@Override
	protected void internalLayout() {

        MHorizontalLayout statsLine01 = new MHorizontalLayout(registeredAssessmentsCard).withSpacing(true);

//		HorizontalLayout statsLine01 = PanelBuilder.HORIZONTAL(registeredUsers, publishedAssessments);
//		statsLine01.setSpacing(true);
//		HorizontalLayout statsLine02 = PanelBuilder.HORIZONTAL(employeeAssessments, completedAssessments);
//		statsLine02.setSpacing(true);
//
		VerticalLayout statsPanel = new MVerticalLayout(statsLine01/*, statsLine02*/);

		Layout fieldsPanel = 
				new MHorizontalLayout(
                        new MFormLayout(startField, endField, publishDeadlineField, activeField),
					    new MFormLayout(selfDeadlineField, assessorDeadlineField, completeDeadlineField)
				);

		addComponents(fieldsPanel, statsPanel);
		setExpandRatio(fieldsPanel, 2);
		setExpandRatio(statsPanel, 3);
	}


    @Override
    public void onEvent(org.vaadin.spring.events.Event<EnterEntityEditViewEvent<PeriodModel>> event) {

        if (PeriodEditView.class.equals(event.getSource().getClass())) {
            final Optional<Period> period = Optional.ofNullable(event.getPayload().getEntity().getWrapped());
            period.ifPresent(period1 -> this.registeredAssessmentsCard
                    .setStatisticSupplier(() -> new CounterStatisticModel("assessments", assessmentService.countAssessmentsFor(period1))));
        }
    }

//    @Override
//    public void onApplicationEvent(final EnterEntityEditViewEvent<PeriodModel> enterEntityEditViewEvent) {
//	    final Optional<Period> period = Optional.ofNullable(enterEntityEditViewEvent.getEntity().getWrapped());
//        period.ifPresent(period1 -> this.registeredAssessmentsCard
//                .setStatisticSupplier(() -> new CounterStatisticModel("assessments", assessmentService.countAssessmentsFor(period1))));
//    }


    //	@Override
//	protected Period buildEntity(Period period) {
//
//		if (period != null) {
//			Map<AssessmentStatus, AssessmentStatusCount> statusCounts = assessmentService.countAssessmentsStatusFor(period);
//			period.getRegisteredAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.All));
//			period.getPublishedAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.Created));
//			period.getEmployeeAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.EmployeeCompleted));
//			period.getCompletedAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.Reviewed));
//		}
//		return period;
//	}

//	private static class AssessmentStatusCountStatusConverter implements MStatsConverter<AssessmentStatusCount> {
//		@Override
//		public MStatsModel convert(final AssessmentStatusCount object) {
//			MStatsModel model = new MStatsModel();
//
//			if (object != null) {
//				model.setStatistic(String.valueOf(object.getCount()));
//
//				switch(object.getStatus()) {
//				case Creating:
//					break;
//				case All:
//					model.setStatisticLabel("Registered users");
//					model.setStatisticInfo("0% up from the previous week");
//					model.setStatisticIcon(I8n.User.ICON);
//					model.setStyleName(MStatsField.STYLE_ERROR);
//					break;
//				case Created:
//					model.setStatisticLabel("Published assessments");
//					model.setStatisticInfo("0% up from the previous week");
//					model.setStatisticIcon(FontAwesome.FILE_O);
//					model.setStyleName(MStatsField.STYLE_INFO);
//					break;
//				case EmployeeCompleted:
//					model.setStatisticLabel("Self-rating assessments completed");
//					model.setStatisticInfo("0% up from the previous week");
//					model.setStatisticIcon(FontAwesome.FILE);
//					model.setStyleName(MStatsField.STYLE_WARNING);
//					break;
//				case ManagerCompleted:
//					model.setStatisticIcon(FontAwesome.FILE_TEXT_O);
//					break;
//				case Reviewed:
//					model.setStatisticLabel("Completed assessments");
//					model.setStatisticInfo("0% up from the previous week");
//					model.setStatisticIcon(FontAwesome.FILE_TEXT);
//					model.setStyleName(MStatsField.STYLE_SUCCESS);
//					break;
//				}
//			} else {
//				model.setStatistic("0");
//			}
//
//			return model;
//		}
//	}

    @PreDestroy
    void destroy() {
        eventBus.unsubscribe(this); // It's good manners to do this, even though we should be automatically unsubscribed when the UI is garbage collected
    }
}
