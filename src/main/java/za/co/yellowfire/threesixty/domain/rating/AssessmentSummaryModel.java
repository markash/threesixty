package za.co.yellowfire.threesixty.domain.rating;

import java.io.Serializable;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@SuppressWarnings("unused")
public class AssessmentSummaryModel implements Serializable {
    private int noOfRatings = 0;
    private double weightingTotal = 0.0;
    private double scoreTotal = 0.0;

    public AssessmentSummaryModel() {}

    public int getNoOfRatings() { return this.noOfRatings; }
    public double getWeightingTotal() { return this.weightingTotal; }
    public double getScoreTotal() { return this.scoreTotal; }

    public void refresh(final Assessment assessment) {
        if (assessment != null) {
            assessment.calculate();
            this.noOfRatings = assessment.getNoOfRatings();
            this.weightingTotal = assessment.getWeightingTotal();
            this.scoreTotal = assessment.getScore();
        }
    }
}
