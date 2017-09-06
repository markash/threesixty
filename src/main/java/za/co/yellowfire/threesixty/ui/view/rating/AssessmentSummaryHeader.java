package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.data.Binder;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import io.threesixty.ui.component.panel.PanelBuilder;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentSummaryModel;
import za.co.yellowfire.threesixty.ui.Style;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class AssessmentSummaryHeader extends HorizontalLayout {

	private final MTextField weightingTotalField = new MTextField();
    private final MTextField noOfRatingsField = new MTextField();
    private final MTextField scoreTotalField = new MTextField();

	private AssessmentSummaryModel assessmentSummary = new AssessmentSummaryModel();
	private Binder<AssessmentSummaryModel> binder = new Binder<>(AssessmentSummaryModel.class);

	AssessmentSummaryHeader(final Component...components) {
		this(Arrays.asList(components));
	}
	
	private AssessmentSummaryHeader(final List<? extends Component> components) {
		super();

		this.binder.setBean(assessmentSummary);
		this.binder.forField(weightingTotalField)
                .withConverter(new StringToDoubleConverter(0.0, "Unable to convert weighting total"))
                .bind("weightingTotal");

		this.binder.forField(scoreTotalField)
                .withConverter(new StringToDoubleConverter(0.0, "Unable to convert score total"))
                .bind("scoreTotal");

        this.binder.forField(noOfRatingsField)
                .withConverter(new StringToDoubleConverter(0.0, "Unable to convert number of ratings"))
                .bind("noOfRatings");
		 		
		HorizontalLayout noOfRatingsPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", new MLabel("Ratings: ").withStyleName(Style.Text.BOLDED), noOfRatingsField);
		HorizontalLayout weightingTotalPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", new MLabel("Weighting: ").withStyleName(Style.Text.BOLDED), weightingTotalField);
		HorizontalLayout scoreTotalPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", new MLabel("Overall Score: ").withStyleName(Style.Text.BOLDED), scoreTotalField);
						
		noOfRatingsPanel.setSpacing(true);
		weightingTotalPanel.setSpacing(true);
		scoreTotalPanel.setSpacing(true);
		
		addComponent(noOfRatingsPanel);
		addComponent(weightingTotalPanel);
		addComponent(scoreTotalPanel);
		
		for (Component component : components) {
			addComponent(component);
		}
		
		setSpacing(true);
        addStyleName("toolbar");	
	}

//	@SuppressWarnings("unchecked")
//	void recalculate() {
//		if (this.assessment != null) {
//			this.assessment.calculate();
//		}
//
////		this.noOfRatingsField.getPropertyDataSource().setValue(getNoOfRatings());
////		this.weightingTotalField.getPropertyDataSource().setValue(getWeightingTotal());
////		this.scoreTotalField.getPropertyDataSource().setValue(getScoreTotal());
//	}
	
	
	public void setAssessment(final Assessment assessment) {
		this.assessmentSummary.refresh(assessment);
	}
}