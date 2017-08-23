package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.data.Binder;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.viritin.fields.MTextField;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class AssessmentSummaryHeader extends HorizontalLayout {
	private Assessment assessment;
	private Binder<AssessmentSummaryHeader> binder = new Binder<>(AssessmentSummaryHeader.class);

	AssessmentSummaryHeader(final Component...components) {
		this(Arrays.asList(components));
	}
	
	private AssessmentSummaryHeader(final List<? extends Component> components) {
		super();

        MTextField weightingTotalField = new MTextField();
        MTextField noOfRatingsField = new MTextField();
        MTextField scoreTotalField = new MTextField();

		this.binder.setBean(this);
		this.binder.bind(weightingTotalField, "weightingTotal");
		this.binder.bind(scoreTotalField,  "scoreTotal");
        this.binder.bind(noOfRatingsField, "noOfRatings");
		 		
		HorizontalLayout noOfRatingsPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", LabelBuilder.build("Ratings: ", Style.Text.BOLDED), noOfRatingsField);
		HorizontalLayout weightingTotalPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", LabelBuilder.build("Weighting: ", Style.Text.BOLDED), weightingTotalField);
		HorizontalLayout scoreTotalPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", LabelBuilder.build("Overall Score: ", Style.Text.BOLDED), scoreTotalField);
						
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
	
	int getNoOfRatings() {
		return assessment != null ? assessment.getNoOfRatings() : 0; 
	}
	
	double getWeightingTotal() {
		return assessment != null ? assessment.getWeightingTotal() : 0.0; 
	}
	
	double getScoreTotal() {
		return assessment != null ? assessment.getScore() : 0.0; 
	}
	
	public void setNoOfRatings(final int noOfRatings) {}
	public void setWeightingTotal(final double weightingTotal) {}
	public void setScoreTotal(final double scoreTotal) {}
	
	@SuppressWarnings("unchecked")
	void recalculate() {
		if (this.assessment != null) {
			this.assessment.calculate();
		}

//		this.noOfRatingsField.getPropertyDataSource().setValue(getNoOfRatings());
//		this.weightingTotalField.getPropertyDataSource().setValue(getWeightingTotal());
//		this.scoreTotalField.getPropertyDataSource().setValue(getScoreTotal());
	}
	
	
	public void setAssessment(final Assessment assessment) {
		this.assessment = assessment;
		recalculate();
	}
}