package za.co.yellowfire.threesixty.ui.view.rating;

import java.util.EventObject;

public class AssessmentDirtyEvent extends EventObject {
    private final double weighting;
    private final double rating;

    AssessmentDirtyEvent(final Object source, double weighting, double rating) {
        super(source);
        this.weighting = weighting;
        this.rating = rating;
    }

    public double getWeighting() {
        return weighting;
    }

    public double getRating() {
        return rating;
    }
}
