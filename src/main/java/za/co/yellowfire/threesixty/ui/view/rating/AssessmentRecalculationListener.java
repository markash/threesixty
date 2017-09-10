package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.event.SerializableEventListener;

@FunctionalInterface
public interface AssessmentRecalculationListener extends SerializableEventListener {
    void assessmentRecalc(final AssessmentRecalculationEvent event);
}