package za.co.yellowfire.threesixty.ui.view.period;

import com.vaadin.event.SerializableEventListener;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentRatingEvent;

@FunctionalInterface
public interface PeriodSelectListener extends SerializableEventListener {
    void period(final PeriodSelectEvent event);
}