package za.co.yellowfire.threesixty.ui.view.period;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.DateField;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import io.threesixty.ui.component.card.CounterStatisticModel;
import io.threesixty.ui.component.card.CounterStatisticsCard;
import io.threesixty.ui.view.AbstractEntityEditForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatusCount;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.ui.I8n;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings("serial")
public class PeriodEntityEditForm extends AbstractEntityEditForm<PeriodModel> {

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
	private final AssessmentService assessmentService;

	PeriodEntityEditForm(
			final AssessmentService assessmentService) {

		super(PeriodModel.class);
		this.assessmentService = assessmentService;

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
				VaadinIcons.USERS,
				new CounterStatisticModel("Registered Assessments", 0),
				"");

        publishedAssessmentsCard = new CounterStatisticsCard(
                VaadinIcons.USERS,
                new CounterStatisticModel("Published Assessments", 0),
                "");

        submittedAssessmentsCard = new CounterStatisticsCard(
                VaadinIcons.USERS,
                new CounterStatisticModel("Submitted Assessments", 0),
                "");

        reviewedAssessmentsCard = new CounterStatisticsCard(
                VaadinIcons.USERS,
                new CounterStatisticModel("Reviewed Assessments", 0),
                "");
	}

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
	protected void updateDependentFields() {
        Consumer<Period> refreshStatisticsCards = (p) -> {
            Map<AssessmentStatus, AssessmentStatusCount> statusCounts = assessmentService.countAssessmentsStatusFor(p);
            statusCounts.forEach((key, e) -> {
                switch (key) {
                    case All:
                        this.registeredAssessmentsCard.getValue().setValue(e.getCount());
                        break;
                    case Created:
                        this.publishedAssessmentsCard.getValue().setValue(e.getCount());
                        break;
                    case EmployeeCompleted:
                        this.submittedAssessmentsCard.getValue().setValue(e.getCount());
                        break;
                    case Reviewed:
                        this.reviewedAssessmentsCard.getValue().setValue(e.getCount());
                        break;
                }
            });
        };

        final Optional<Period> period = Optional.ofNullable(getValue().getWrapped());
        period.ifPresent(refreshStatisticsCards);
	}
}
