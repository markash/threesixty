package za.co.yellowfire.threesixty.ui.view.rating;

import za.co.yellowfire.threesixty.domain.rating.AssessmentRating;

import java.util.EventObject;

public class AssessmentRatingEvent extends EventObject {
    private final AssessmentRating rating;
    private final Action action;

    public AssessmentRatingEvent(
            final Object source,
            final AssessmentRating rating,
            final Action action) {
        super(source);
        this.rating = rating;
        this.action = action;
    }

    public AssessmentRating getRating() { return rating; }
    public Action getAction() { return action; }

    public enum Action {
        ADD,
        REMOVE
    }
}