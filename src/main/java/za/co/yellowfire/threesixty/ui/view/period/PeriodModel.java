package za.co.yellowfire.threesixty.ui.view.period;

import org.springframework.data.domain.Persistable;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodDeadline;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Optional;

public class PeriodModel implements Persistable<Serializable> {
    public static final long serialVersionUID = 1L;

    static final String FIELD_ID = "id";
    static final String FIELD_START = "start";
    static final String FIELD_END = "end";
    static final String FIELD_DEADLINE_PUBLISHED = "publishAssessmentDeadline";
    static final String FIELD_DEADLINE_COMPLETED = "completeAssessmentDeadline";
    static final String FIELD_DEADLINE_SELF = "selfAssessmentDeadline";
    static final String FIELD_DEADLINE_MANAGER = "managerAssessmentDeadline";
    static final String FIELD_ACTIVE = "active";

    private final Period period;

    PeriodModel() {
        this.period = new Period();
    }

    PeriodModel(Period period) {
        this.period = Optional.ofNullable(period).orElse(new Period());
    }

    @Override
    public String getId() { return this.period.getId(); }
    public LocalDate getStart() { return this.period.getStart(); }
    public LocalDate getEnd() { return this.period.getEnd(); }
    @SuppressWarnings("unused")
    public LocalDate getPublishAssessmentDeadline() { return this.period.getDeadline().getPublishAssessment(); }
    @SuppressWarnings("unused")
    public LocalDate getCompleteAssessmentDeadline() { return this.period.getDeadline().getCompleteAssessment(); }
    @SuppressWarnings("unused")
    public LocalDate getSelfAssessmentDeadline() { return this.period.getDeadline().getSelfAssessment(); }
    @SuppressWarnings("unused")
    public LocalDate getManagerAssessmentDeadline() { return this.period.getDeadline().getManagerAssessment(); }
    public boolean isActive() { return this.period.isActive(); }

    public void setStart(final LocalDate value) { this.period.setStart(value); }
    public void setEnd(final LocalDate value) { this.period.setEnd(value); }
    @SuppressWarnings("unused")
    public void setPublishAssessmentDeadline(final LocalDate value) { ifAbsentDeadlineCreate(); this.period.getDeadline().setPublishAssessment(value); }
    @SuppressWarnings("unused")
    public void setCompleteAssessmentDeadline(final LocalDate value) { ifAbsentDeadlineCreate(); this.period.getDeadline().setCompleteAssessment(value); }
    @SuppressWarnings("unused")
    public void setSelfAssessmentDeadline(final LocalDate value) { ifAbsentDeadlineCreate(); this.period.getDeadline().setSelfAssessment(value); }
    @SuppressWarnings("unused")
    public void setManagerAssessmentDeadline(final LocalDate value) { ifAbsentDeadlineCreate(); this.period.getDeadline().setManagerAssessment(value); }

    private void ifAbsentDeadlineCreate() {
        if (this.period.getDeadline() == null) this.period.setDeadline(new PeriodDeadline());
    }

    @Override
    public boolean isNew() { return this.period.isNew(); }
    public Period getWrapped() { return this.period; }
}
