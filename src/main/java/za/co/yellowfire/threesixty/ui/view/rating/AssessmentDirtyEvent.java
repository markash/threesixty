package za.co.yellowfire.threesixty.ui.view.rating;

import java.util.EventObject;

public class AssessmentDirtyEvent extends EventObject {
    private boolean recalculationRequired;
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public AssessmentDirtyEvent(final Object source, boolean recalculationRequired) {
        super(source);
        this.recalculationRequired = recalculationRequired;
    }

    public boolean isRecalculationRequired() {
        return recalculationRequired;
    }
}
