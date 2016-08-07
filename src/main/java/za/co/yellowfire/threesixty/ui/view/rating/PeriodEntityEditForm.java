package za.co.yellowfire.threesixty.ui.view.rating;

import java.util.Map;

import org.vaadin.viritin.fields.MDateField;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatusCount;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.field.MStatsConverter;
import za.co.yellowfire.threesixty.ui.component.field.MStatsField;
import za.co.yellowfire.threesixty.ui.component.field.MStatsModel;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class PeriodEntityEditForm extends AbstractEntityEditForm<Period> {

	@PropertyId(Period.FIELD_START)
	private MDateField startField = new MDateField(I8n.Period.Fields.START);
	@PropertyId(Period.FIELD_END)
	private MDateField endField = new MDateField(I8n.Period.Fields.END);
	@PropertyId(Period.FIELD_DEADLINE_PUBLISH)
	private MDateField publishDeadlineField = new MDateField(I8n.Period.Fields.DEADLINE_PUBLISH);
	@PropertyId(Period.FIELD_DEADLINE_COMPLETE)
	private MDateField completeDeadlineField = new MDateField(I8n.Period.Fields.DEADLINE_COMPLETE);
	@PropertyId(Period.FIELD_DEADLINE_SELF_ASSESSMENT)
	private MDateField selfDeadlineField = new MDateField(I8n.Period.Fields.DEADLINE_SELF_ASSESSMENT);
	@PropertyId(Period.FIELD_DEADLINE_ASSESSOR_ASSESSMENT)
	private MDateField assessorDeadlineField = new MDateField(I8n.Period.Fields.DEADLINE_ASSESSOR_ASSESSMENT);
	@PropertyId(Period.FIELD_ACTIVE)
	private CheckBox activeField = new CheckBox(I8n.Period.Fields.ACTIVE);

	@PropertyId(Period.FIELD_REGISTERED_ASSESSMENTS)
	private MStatsField registeredUsers = new MStatsField("0", "Registered users", "0% up from the previous week", I8n.User.ICON, MStatsField.STYLE_ERROR);
	@PropertyId(Period.FIELD_PUBLISHED_ASSESSMENTS)
	private MStatsField publishedAssessments = new MStatsField("0", "Published assessments", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_INFO);
	@PropertyId(Period.FIELD_EMPLOYEE_ASSESSMENTS)
	private MStatsField employeeAssessments = new MStatsField("0", "Self-rating assessments completed", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_WARNING);
	@PropertyId(Period.FIELD_COMPLETED_ASSESSMENTS)
	private MStatsField completedAssessments = new MStatsField("0", "Completed assessments", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_SUCCESS);
	
	@SuppressWarnings("unused")
	private final PeriodService service;
	private final AssessmentService assessmentService;
	private final MStatsConverter<AssessmentStatusCount> assessmentCountConverter = new AssessmentStatusCountStatusConverter();
	
	private String[] nestedProperties = new String[] {
			Period.FIELD_DEADLINE_PUBLISH, 
			Period.FIELD_DEADLINE_COMPLETE, 
			Period.FIELD_DEADLINE_SELF_ASSESSMENT,
			Period.FIELD_DEADLINE_ASSESSOR_ASSESSMENT};
	
	public PeriodEntityEditForm(final PeriodService periodService, final AssessmentService assessmentService) {
		this.service = periodService;
		this.assessmentService = assessmentService;
		
		this.startField.setDateFormat(I8n.Format.DATE);
		this.endField.setDateFormat(I8n.Format.DATE);
		this.publishDeadlineField.setDateFormat(I8n.Format.DATE);
		this.completeDeadlineField.setDateFormat(I8n.Format.DATE);
		this.selfDeadlineField.setDateFormat(I8n.Format.DATE);
		this.assessorDeadlineField.setDateFormat(I8n.Format.DATE);
	}
	
	/**
	 * Returns the list of nested properties that the form group should bind to
	 * @returns An array of nested properties in Java object notation
	 */
	@Override
	protected String[] getNestedProperties() { return nestedProperties; }

	@Override
	protected void internalLayout() {

		HorizontalLayout statsLine01 = PanelBuilder.HORIZONTAL(registeredUsers, publishedAssessments);
		statsLine01.setSpacing(true);
		HorizontalLayout statsLine02 = PanelBuilder.HORIZONTAL(employeeAssessments, completedAssessments);
		statsLine02.setSpacing(true);
		
		VerticalLayout statsPanel = PanelBuilder.VERTICAL(statsLine01, statsLine02);

		Layout fieldsPanel = 
				PanelBuilder.HORIZONTAL(
					PanelBuilder.FORM(
							startField, 
							endField,
							publishDeadlineField
							),
					PanelBuilder.FORM(
							selfDeadlineField,
							assessorDeadlineField,
							completeDeadlineField
							)
				);
		addComponents(fieldsPanel, statsPanel);
		setExpandRatio(fieldsPanel, 2);
		setExpandRatio(statsPanel, 3);
	}
	
	@Override
	protected Period buildEmpty() { return Period.EMPTY(); }
	
	@Override
	protected Period buildEntity(Period period) {
		
		if (period != null) {
			Map<AssessmentStatus, AssessmentStatusCount> statusCounts = assessmentService.countAssessmentsStatusFor(period);
			period.getRegisteredAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.All));
			period.getPublishedAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.Created));
			period.getEmployeeAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.EmployeeCompleted));
			period.getCompletedAssessments().updateWith(assessmentCountConverter, statusCounts.get(AssessmentStatus.Reviewed));
		}
		return period;
	}

	private static class AssessmentStatusCountStatusConverter implements MStatsConverter<AssessmentStatusCount> {
		@Override
		public MStatsModel convert(final AssessmentStatusCount object) {
			MStatsModel model = new MStatsModel();
			
			if (object != null) {
				model.setStatistic(String.valueOf(object.getCount()));
				
				switch(object.getStatus()) {
				case Creating:
					break;
				case All:
					model.setStatisticLabel("Registered users");
					model.setStatisticInfo("0% up from the previous week");
					model.setStatisticIcon(I8n.User.ICON);
					model.setStyleName(MStatsField.STYLE_ERROR);
					break;
				case Created:
					model.setStatisticLabel("Published assessments");
					model.setStatisticInfo("0% up from the previous week");
					model.setStatisticIcon(FontAwesome.FILE_O);
					model.setStyleName(MStatsField.STYLE_INFO);
					break;
				case EmployeeCompleted:
					model.setStatisticLabel("Self-rating assessments completed");
					model.setStatisticInfo("0% up from the previous week");
					model.setStatisticIcon(FontAwesome.FILE);
					model.setStyleName(MStatsField.STYLE_WARNING);
					break;
				case ManagerCompleted:
					model.setStatisticIcon(FontAwesome.FILE_TEXT_O);
					break;
				case Reviewed:
					model.setStatisticLabel("Completed assessments");
					model.setStatisticInfo("0% up from the previous week");
					model.setStatisticIcon(FontAwesome.FILE_TEXT);
					model.setStyleName(MStatsField.STYLE_SUCCESS);
					break;
				}
			} else {
				model.setStatistic("0");
			}
			
			return model;
		}
	}
}
