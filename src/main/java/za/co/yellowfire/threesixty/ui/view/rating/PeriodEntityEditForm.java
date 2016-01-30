package za.co.yellowfire.threesixty.ui.view.rating;

import org.vaadin.viritin.fields.MDateField;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.field.MStatsField;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class PeriodEntityEditForm extends AbstractEntityEditForm<Period> {

	@PropertyId(Period.FIELD_START)
	private MDateField startField = new MDateField(I8n.Period.Fields.START);
	@PropertyId(Period.FIELD_END)
	private MDateField endField = new MDateField(I8n.Period.Fields.END);
	@PropertyId(Assessment.FIELD_ACTIVE)
	private CheckBox activeField = new CheckBox(I8n.Period.Fields.ACTIVE);
	
	@SuppressWarnings("unused")
	private final PeriodService service;

	private MStatsField registeredUsers = new MStatsField("0", "Registered users", "0% up from the previous week", I8n.User.ICON, MStatsField.STYLE_ERROR);
	private MStatsField publishedAssessments = new MStatsField("0", "Published assessments", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_INFO);
	private MStatsField employeeAssessments = new MStatsField("0", "Self-rating assessments completed", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_WARNING);
	private MStatsField completedAssessments = new MStatsField("0", "Completed assessments", "0% up from the previous week", I8n.Assessment.ICON, MStatsField.STYLE_SUCCESS);
	
	public PeriodEntityEditForm(final PeriodService service) {
		this.service = service;
		this.startField.setDateFormat(I8n.Format.DATE);
		this.endField.setDateFormat(I8n.Format.DATE);
	}
	
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
							new MDateField("Publish deadline")
							),
					PanelBuilder.FORM(
							new MDateField("Self-assessment deadline"),
							new MDateField("Manager assessment deadline"),
							new MDateField("Complete deadline")
							)
				);
		addComponents(fieldsPanel, statsPanel);
		setExpandRatio(fieldsPanel, 2);
		setExpandRatio(statsPanel, 3);
	}
	
	@Override
	protected Period buildEmpty() {
		return Period.EMPTY();
	}
}
