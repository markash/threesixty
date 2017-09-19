package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.field.MDoubleLabel;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("serial")
public class AssessmentSummaryHeader extends HorizontalLayout {

	private final MDoubleLabel weightingTotalField = new MDoubleLabel();
    private final MDoubleLabel noOfRatingsField = new MDoubleLabel();
    private final MDoubleLabel scoreTotalField = new MDoubleLabel();

	private AssessmentSummaryModel assessmentSummary = new AssessmentSummaryModel();
//	private Binder<AssessmentSummaryModel> binder = new Binder<>(AssessmentSummaryModel.class);

	AssessmentSummaryHeader(final Component...components) {
		this(Arrays.asList(components));
	}
	
	private AssessmentSummaryHeader(final List<? extends Component> components) {
		super();

		this.weightingTotalField.setValue(assessmentSummary.getWeightingTotal());
		this.noOfRatingsField.setValue(assessmentSummary.getNoOfRatings());
		this.scoreTotalField.setValue(assessmentSummary.getScoreTotal());

		addComponent(
		        new MHorizontalLayout(
		                new MLabel("Ratings: ").withStyleName(Style.Text.BOLDED), noOfRatingsField)
                        .withStyleName("rating-summary-item")
                        .withSpacing(true));
        addComponent(
                new MHorizontalLayout(
                        new MLabel("Weightings: ").withStyleName(Style.Text.BOLDED), weightingTotalField)
                        .withStyleName("rating-summary-item")
                        .withSpacing(true));

        addComponent(
                new MHorizontalLayout(
                        new MLabel("Overall Score: ").withStyleName(Style.Text.BOLDED), scoreTotalField)
                        .withStyleName("rating-summary-item")
                        .withSpacing(true));

		for (Component component : components) {
			addComponent(component);
		}
		
		setSpacing(true);
		setMargin(false);
        addStyleName("toolbar");	
	}

//	@SuppressWarnings("unchecked")
	void recalculate(final AssessmentRecalculationEvent event) {
        this.assessmentSummary.refresh(event);
		this.noOfRatingsField.setValue(assessmentSummary.getNoOfRatings());
		this.weightingTotalField.setValue(assessmentSummary.getWeightingTotal());
		this.scoreTotalField.setValue(assessmentSummary.getScoreTotal());
	}

	public void setAssessment(final Assessment assessment) {
        this.assessmentSummary.refresh(assessment);
        this.weightingTotalField.setValue(assessmentSummary.getWeightingTotal());
        this.noOfRatingsField.setValue(assessmentSummary.getNoOfRatings());
        this.scoreTotalField.setValue(assessmentSummary.getScoreTotal());
	}
}