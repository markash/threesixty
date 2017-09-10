package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.event.SerializableEventListener;

@FunctionalInterface
public interface AssessmentRatingListener extends SerializableEventListener {
    void assessmentRating(final AssessmentRatingEvent event);
}