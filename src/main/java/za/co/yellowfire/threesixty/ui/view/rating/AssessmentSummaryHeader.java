package za.co.yellowfire.threesixty.ui.view.rating;

import java.util.Arrays;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;

@SuppressWarnings("serial")
public class AssessmentSummaryHeader extends HorizontalLayout {
	private Assessment assessment;
	
	private BeanItem<AssessmentSummaryHeader> summaryFieldGroup;
	private Label weightingTotalField;
	private Label noOfRatingsField;
	private Label scoreTotalField;
	
	public AssessmentSummaryHeader(final Component...components) {
		this(Arrays.asList(components));
	}
	
	public AssessmentSummaryHeader(final List<? extends Component> components) {
		super();
				
		this.summaryFieldGroup = new BeanItem<>(this);
		this.weightingTotalField = new Label(this.summaryFieldGroup.getItemProperty("weightingTotal"));
		this.scoreTotalField = new Label(this.summaryFieldGroup.getItemProperty("scoreTotal"));
		this.noOfRatingsField = new Label(this.summaryFieldGroup.getItemProperty("noOfRatings"));
		 		
		HorizontalLayout noOfRatingsPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", LabelBuilder.build("Ratings: ", Style.Text.BOLDED), this.noOfRatingsField);
		HorizontalLayout weightingTotalPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", LabelBuilder.build("Weighting: ", Style.Text.BOLDED), this.weightingTotalField);
		HorizontalLayout scoreTotalPanel = 
				PanelBuilder.HORIZONTAL("rating-summary-item", LabelBuilder.build("Overall Score: ", Style.Text.BOLDED), this.scoreTotalField);
						
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
	
	public int getNoOfRatings() { 
		return assessment != null ? assessment.getNoOfRatings() : 0; 
	}
	
	public double getWeightingTotal() { 
		return assessment != null ? assessment.getWeightingTotal() : 0.0; 
	}
	
	public double getScoreTotal() { 
		return assessment != null ? assessment.getScore() : 0.0; 
	}
	
	public void setNoOfRatings(final int noOfRatings) {}
	public void setWeightingTotal(final double weightingTotal) {}
	public void setScoreTotal(final double scoreTotal) {}
	
	@SuppressWarnings("unchecked")
	public void recalculate() {
		if (this.assessment != null) {
			this.assessment.calculate();
		}
		
		this.noOfRatingsField.getPropertyDataSource().setValue(getNoOfRatings());
		this.weightingTotalField.getPropertyDataSource().setValue(getWeightingTotal());
		this.scoreTotalField.getPropertyDataSource().setValue(getScoreTotal());
	}
	
	
	public void setAssessment(final Assessment assessment) {
		this.assessment = assessment;
		recalculate();
	}
}