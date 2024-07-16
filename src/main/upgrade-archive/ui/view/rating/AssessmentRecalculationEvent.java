package za.co.yellowfire.threesixty.ui.view.rating;

import java.util.EventObject;

public class AssessmentRecalculationEvent extends EventObject {
    private final int noOfRatings;
    private final double weightingTotal;
    private final double scoreTotal;

    public AssessmentRecalculationEvent(
            final Object source,
            int noOfRatings,
            double weightingTotal,
            double scoreTotal) {
        super(source);
        this.noOfRatings = noOfRatings;
        this.weightingTotal = weightingTotal;
        this.scoreTotal = scoreTotal;
    }

    public int getNoOfRatings() { return noOfRatings; }
    public double getWeightingTotal() { return weightingTotal; }
    public double getScoreTotal() { return scoreTotal; }
}