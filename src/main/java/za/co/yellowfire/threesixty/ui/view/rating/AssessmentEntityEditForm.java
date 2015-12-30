package za.co.yellowfire.threesixty.ui.view.rating;

import org.vaadin.viritin.button.MButton;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.button.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.component.rating.AssessmentRatingsField;
import za.co.yellowfire.threesixty.ui.component.rating.AssessmentRatingsField.ChangeEvent;
import za.co.yellowfire.threesixty.ui.component.rating.AssessmentRatingsField.RecalculationEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;

@SuppressWarnings("serial")
public class AssessmentEntityEditForm extends AbstractEntityEditForm<Assessment> {

	@PropertyId(Assessment.FIELD_MANAGER)
	private MComboBox managerField;
	@PropertyId(Assessment.FIELD_EMPLOYEE)
	private MComboBox employeeField;
	@PropertyId(Assessment.FIELD_PERIOD)
	private MComboBox periodField;
	@PropertyId(Assessment.FIELD_RATINGS)
	private AssessmentRatingsField ratingsField;
	
	private AssessmentService service;
	private HeaderButtons headerButtons;
	private MButton addButton = new MButton("Add").withIcon(FontAwesome.PLUS_CIRCLE).withListener(this::onAddAssessment);
	
	private AssessmentRatingSummary summary;
	private BeanItem<AssessmentRatingSummary> summaryFieldGroup;
	private Label weightingTotalField;
	private Label noOfRatingsField;
	private Label ratingTotalField;
	
	public AssessmentEntityEditForm(AssessmentService service) {
		this.service = service;
		
		this.managerField = 
				new MComboBox(I8n.Assessment.Fields.MANAGER, new IndexedContainer(service.findActiveUsers()))
					.withWidth(100.0f, Unit.PERCENTAGE)
					.withReadOnly(true);
		
		this.employeeField = 
				new MComboBox(I8n.Assessment.Fields.EMPLOYEE, new IndexedContainer(service.findActiveUsers()))
					.withWidth(100.0f, Unit.PERCENTAGE)
					.valueChangeListener(this::onEmployeeSelected);
		
		this.periodField =
				new MComboBox(I8n.Assessment.Fields.PERIOD, new IndexedContainer(service.findActivePeriods()))
					.withWidth(100.0f, Unit.PERCENTAGE);
		
		this.ratingsField = new AssessmentRatingsField(service.findPossibleRatings(), service.findPossibleWeightings());
		this.ratingsField.setAssessmentRatingListener(this::onAssessmentRatingChange);
		this.ratingsField.setRecalculationListener(this::onAssessmentRecalculation);
		
		this.summary = new AssessmentRatingSummary();
		this.summaryFieldGroup = new BeanItem<>(this.summary);
		this.weightingTotalField = new Label(this.summaryFieldGroup.getItemProperty("weightingTotal"));
		this.ratingTotalField = new Label(this.summaryFieldGroup.getItemProperty("ratingTotal"));
		this.noOfRatingsField = new Label(this.summaryFieldGroup.getItemProperty("noOfRatings"));
		 		
		HorizontalLayout noOfRatingsPanel = 
				PanelBuilder.HORIZONTAL("some-style", LabelBuilder.build("Ratings: ", Style.Text.BOLDED), this.noOfRatingsField);
		HorizontalLayout weightingTotalPanel = 
				PanelBuilder.HORIZONTAL("some-style", LabelBuilder.build("Weighting: ", Style.Text.BOLDED), this.weightingTotalField);
		HorizontalLayout ratingTotalPanel = 
				PanelBuilder.HORIZONTAL("some-style", LabelBuilder.build("Overall Score: ", Style.Text.BOLDED), this.ratingTotalField);
						
		noOfRatingsPanel.setSpacing(true);
		weightingTotalPanel.setSpacing(true);
		ratingTotalPanel.setSpacing(true);
		
		this.headerButtons = new HeaderButtons(noOfRatingsPanel, weightingTotalPanel, ratingTotalPanel, addButton);
	}
	
	@Override
	protected void internalLayout() {
		this.ratingsField.setAssessmentStatus(getValue().getStatus());
		this.ratingsField.setPossibleRatings(service.findPossibleRatings());
		this.ratingsField.setPossibleWeightings(service.findPossibleWeightings());
		this.summary.updateUsing(getValue().getAssessmentRatings());
		
		Label headerCaption = 
				LabelBuilder.build(FontAwesome.BARCODE.getHtml() +  " Assessent ratings", 
						ContentMode.HTML, 
						ValoTheme.LABEL_H3, 
						ValoTheme.LABEL_NO_MARGIN);
		
		HorizontalLayout header = PanelBuilder.HORIZONTAL(Style.AssessmentRating.HEADER, 
				headerCaption,
				this.headerButtons);
		
		addComponent(PanelBuilder.FORM(
        		PanelBuilder.HORIZONTAL(idField, employeeField, managerField, periodField),
        		header,
        		ratingsField
        		));
	}
	
	@Override
	protected Assessment buildEmpty() {
		return Assessment.EMPTY();
	}
	
	public void onAddAssessment(final ClickEvent event) {
		ratingsField.addAssessmentRating();
	}
	
	public void onAssessmentRatingChange(final ChangeEvent event) {
	}
	
	@SuppressWarnings("unchecked")
	public void onAssessmentRecalculation(final RecalculationEvent event) {
		this.summary.updateUsing(event);
		
		this.noOfRatingsField.getPropertyDataSource().setValue(event.getNoOfRatings());
		this.weightingTotalField.getPropertyDataSource().setValue(event.getWeightingTotal());
		this.ratingTotalField.getPropertyDataSource().setValue(event.getRatingTotal());
	}
	
	public void onEmployeeSelected(Property.ValueChangeEvent event) {
		User user = (User) event.getProperty().getValue();
		if (user != null && user.getReportsTo() != null) {
			this.managerField.select(user.getReportsTo());
			this.managerField.markAsDirty();
		} else {
			this.managerField.select(null);
			this.managerField.markAsDirty();
		}
	}
}
