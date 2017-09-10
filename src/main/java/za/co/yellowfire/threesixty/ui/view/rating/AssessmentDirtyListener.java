package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.event.SerializableEventListener;

@FunctionalInterface
public interface AssessmentDirtyListener extends SerializableEventListener {
    void assessmentDirty(final AssessmentDirtyEvent event);
}