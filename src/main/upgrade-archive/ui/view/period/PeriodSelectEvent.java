package za.co.yellowfire.threesixty.ui.view.period;

import java.util.EventObject;
import java.util.Optional;

public class PeriodSelectEvent extends EventObject {
    private final PeriodModel period;
    private final Action action;

    public PeriodSelectEvent(
            final Object source,
            final PeriodModel period,
            final Action action) {
        super(source);
        this.period = period;
        this.action = action;
    }

    public Optional<PeriodModel> getPeriod() { return Optional.ofNullable(this.period); }
    public Action getAction() { return action; }

    public enum Action {
        OK,
        CANCEL
    }
}