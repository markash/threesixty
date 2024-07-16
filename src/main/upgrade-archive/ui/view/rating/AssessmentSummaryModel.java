package za.co.yellowfire.threesixty.ui.view.rating;

import za.co.yellowfire.threesixty.domain.rating.Assessment;

import java.io.Serializable;

/**
 * @author Mark P Ashworth (mp.ashworth@gmail.com)
 */
@SuppressWarnings("unused")
class AssessmentSummaryModel implements Serializable {
    private int noOfRatings = 0;
    private double weightingTotal = 0.0;
    private double scoreTotal = 0.0;

    AssessmentSummaryModel() {}

    int getNoOfRatings() { return this.noOfRatings; }
    double getWeightingTotal() { return this.weightingTotal; }
    double getScoreTotal() { return this.scoreTotal; }

    AssessmentSummaryModel addRating(final double weighting, final double rating) {
        this.noOfRatings++;
        this.weightingTotal += weighting;
        this.scoreTotal += rating;
        return this;
    }

    void refresh(final AssessmentRecalculationEvent event) {
        if (event != null) {
            this.noOfRatings = event.getNoOfRatings();
            this.weightingTotal = event.getWeightingTotal();
            this.scoreTotal = event.getScoreTotal();
        }
    }

    void refresh(final Assessment assessment) {
        if (assessment != null) {
            this.noOfRatings = assessment.getNoOfRatings();
            this.weightingTotal = assessment.getWeightingTotal();
            this.scoreTotal = assessment.getScore();
        }
    }
}
