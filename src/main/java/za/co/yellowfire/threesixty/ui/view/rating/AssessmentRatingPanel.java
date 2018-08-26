package za.co.yellowfire.threesixty.ui.view.rating;

import com.github.markash.ui.component.panel.PanelBuilder;
import com.vaadin.data.*;
import com.vaadin.data.converter.StringToDoubleConverter;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.EventRouter;
import com.vaadin.shared.Registration;
import com.vaadin.ui.*;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.label.MLabel;
import za.co.yellowfire.threesixty.domain.rating.AssessmentRating;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.field.MDoubleLabel;

import java.util.Collection;

public class AssessmentRatingPanel extends GridLayout {

    private MTextField idField = new MTextField("Id").withReadOnly(true);
    private TextArea measurementField = new TextArea("Measurement");
    private TextArea managerCommentField = new TextArea("Manager Comment");
    private TextArea employeeCommentField = new TextArea("Employee Comment");
    private ComboBox<Discipline> areaField;
    private ComboBox<Double> weightField = new ComboBox<>("Weight");
    private ComboBox<Double> ratingField = new ComboBox<>("Rating");
    private MTextField scoreField = new MTextField("Score").withWidth(100.0f, Unit.PERCENTAGE).withReadOnly(true);

    /* Read only rating panel */
    private MDoubleLabel employeeRatingField;
    private MDoubleLabel managerRatingField;
    private MDoubleLabel reviewRatingField;

    private User currentUser;
    private AssessmentRating rating;
    private Binder<AssessmentRating> binder = new Binder<>(AssessmentRating.class);
    private Registration valueChangeRegistration;
    private EventRouter eventRouter;

    AssessmentRatingPanel(
            final AssessmentRating rating,
            final User currentUser,
            final Collection<Double> possibleRatings,
            final Collection<Double> possibleWeightings,
            final Collection<Discipline> disciplines) {
        super(3, 3);

        setWidth(100.0f, Unit.PERCENTAGE);
        setMargin(false);
        setSpacing(true);

        this.rating = rating;
        this.currentUser = currentUser;

        this.areaField = new ComboBox<>("Performance Area", disciplines);
        this.areaField.setWidth(100.0f, Unit.PERCENTAGE);

        ListDataProvider<Double> ratingsProvider = new ListDataProvider<>(possibleRatings);
        this.ratingField.setDataProvider(ratingsProvider);
        this.ratingField.setWidth(100.0f, Unit.PERCENTAGE);

        ListDataProvider<Double> weightingsProvider = new ListDataProvider<>(possibleWeightings);
        this.weightField.setDataProvider(weightingsProvider);
        this.weightField.setWidth(100.0f, Unit.PERCENTAGE);

        /* Setup read only ratings panel */
        this.employeeRatingField = new MDoubleLabel(rating.getEmployeeRating());
		this.managerRatingField = new MDoubleLabel(rating.getManagerRating());
		this.reviewRatingField = new MDoubleLabel(rating.getReviewRating());

        measurementField.setRows(15);
        measurementField.setWidth(100.0f, Unit.PERCENTAGE);
        managerCommentField.setRows(5);
        managerCommentField.setWidth(100.0f, Unit.PERCENTAGE);
        employeeCommentField.setRows(5);
        employeeCommentField.setWidth(100.0f, Unit.PERCENTAGE);

		/* Bind */
        this.binder.forField(idField).bind("id");
        this.binder.forField(areaField).bind("performanceArea");
        this.binder.forField(measurementField).bind("measurement");
        this.binder.forField(managerCommentField).bind("managerComment");
        this.binder.forField(employeeCommentField).bind("employeeComment");
        this.binder.forField(weightField).bind("weight");
        this.binder.forField(ratingField).bind("rating");
        this.binder.forField(scoreField).withConverter(new StringToDoubleConverter(0.0, "Unable to convert weighting")).bind("score");
        this.binder.readBean(this.rating);
        this.binder.addStatusChangeListener(event -> {
            boolean isValid = event.getBinder().isValid();
            boolean hasChanges = event.getBinder().hasChanges();

            if (isValid && hasChanges) {
                /* Bubble up the status change event */
                getEventRouter().fireEvent(event);
                /* Fire the assessment recalculation event */
                getEventRouter().fireEvent(new AssessmentRecalculationEvent(this, 0, this.weightField.getValue(), this.ratingField.getValue()));
            }
        });

        /* Column 0 */
        addComponent(areaField, 0, 0);
        addComponent(managerCommentField, 0, 1);
        addComponent(employeeCommentField, 0, 2);

		/* Column 1 */
        addComponent(measurementField, 1, 0, 1, 2);

		/* Column 2 */
        Label ratingLabel = new MLabel("Self-rating: ").withStyleName(Style.Text.BOLDED);
        HorizontalLayout employeeRatingPanel =
                PanelBuilder.HORIZONTAL(
                        "rating-summary-item",
                        ratingLabel,
                        this.employeeRatingField);
        employeeRatingPanel.setExpandRatio(ratingLabel, 3);
        employeeRatingPanel.setExpandRatio(this.employeeRatingField, 1);
        employeeRatingPanel.setSpacing(true);

        ratingLabel =  new MLabel("Manager rating: ").withStyleName(Style.Text.BOLDED);
        HorizontalLayout managerRatingPanel =
                PanelBuilder.HORIZONTAL(
                        "rating-summary-item",
                        ratingLabel,
                        this.managerRatingField);
        managerRatingPanel.setExpandRatio(ratingLabel, 3);
        managerRatingPanel.setExpandRatio(this.managerRatingField, 1);
        managerRatingPanel.setSpacing(true);

        ratingLabel = new MLabel("Review rating: ").withStyleName(Style.Text.BOLDED);
        HorizontalLayout reviewRatingPanel =
                PanelBuilder.HORIZONTAL(
                        "rating-summary-item",
                        ratingLabel,
                        this.reviewRatingField);
        reviewRatingPanel.setExpandRatio(ratingLabel, 3);
        reviewRatingPanel.setExpandRatio(this.reviewRatingField, 1);
        reviewRatingPanel.setSpacing(true);

        VerticalLayout ratingPanel;
        switch (rating.getAssessment().getStatus()) {
            case Created:
                ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField, employeeRatingPanel);
                break;
            case EmployeeCompleted:
                ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField, employeeRatingPanel, managerRatingPanel);
                break;
            case ManagerCompleted:
            case Reviewed:
                ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField, employeeRatingPanel, managerRatingPanel, reviewRatingPanel);
                break;
            default:
                ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField);
        }
        addComponent(ratingPanel, 2, 0, 2, 2);

        setColumnExpandRatio(0, 3f);
        setColumnExpandRatio(1, 3f);
        setColumnExpandRatio(2, 1f);

        updateFieldAccess();
        registerValueChangeListener();
    }

    void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
    }

    double getWeighting() {
        return this.weightField.getValue();
    }

    double getRating() {
        return this.ratingField.getValue();
    }

    void updateFieldAccess() {

        this.areaField.setReadOnly(isAreaReadOnly());
        this.measurementField.setReadOnly(isMeasurementReadOnly());
        this.managerCommentField.setReadOnly(isManagerCommentReadOnly());
        this.employeeCommentField.setReadOnly(isEmployeeCommentReadOnly());
        this.weightField.setReadOnly(isWeightReadOnly());
        this.ratingField.setReadOnly(isRatingReadOnly());
    }

    /**
     * Determine whether the panel is valid or has any validation warnings
     * @return True if the panel is valid else false
     */
    boolean isValid() {
        return this.binder.isValid();
    }

    /**
     * Determine whether the panel has changes
     * @return True if the panel has changes else false
     */
    boolean hasChanges() {
        return this.binder.hasChanges();
    }

    public AssessmentRating getValue() {
        return this.rating;
    }

    private boolean isCurrentUserAdmin() {
        return this.currentUser != null && this.currentUser.isAdmin();
    }

    private boolean isCurrentUserManager() {
        return getValue().getAssessment() != null &&
                getValue().getAssessment().getManager() != null &&
                getValue().getAssessment().getManager().equals(currentUser);
    }

    private boolean isCurrentUserEmployee() {
        return getValue().getAssessment() != null &&
                getValue().getAssessment().getEmployee() != null &&
                getValue().getAssessment().getEmployee().equals(currentUser);
    }

    @SuppressWarnings("unused")
    public boolean isCreatingStatus() {
        return getValue().getAssessment().getStatus() == AssessmentStatus.Creating;
    }

    private boolean isCreatedStatus() {
        return getValue().getAssessment().getStatus() == AssessmentStatus.Created;
    }

    private boolean isEmployeeCompletedStatus() {
        return getValue().getAssessment().getStatus() == AssessmentStatus.EmployeeCompleted;
    }

    private boolean isManagerCompletedStatus() {
        return getValue().getAssessment().getStatus() == AssessmentStatus.ManagerCompleted;
    }

    private boolean isReviewedStatus() {
        return getValue().getAssessment().getStatus() == AssessmentStatus.Reviewed;
    }

    private boolean isAreaReadOnly() {
        return !((isCurrentUserAdmin() || isCurrentUserManager()) && !isReviewedStatus());
    }

    private boolean isMeasurementReadOnly() {
        return !((isCurrentUserAdmin() || isCurrentUserManager()) && !isReviewedStatus());
    }

    private boolean isEmployeeCommentReadOnly() {
        return !(isCurrentUserEmployee() && (isCreatedStatus()));
    }

    private boolean isManagerCommentReadOnly() {
        return !(isCurrentUserManager() && (isEmployeeCompletedStatus() || isManagerCompletedStatus()));
    }

    private boolean isWeightReadOnly() {
        return !((isCurrentUserAdmin() || isCurrentUserManager()) && !isReviewedStatus());
    }

    private boolean isRatingReadOnly() {
        switch(getValue().getAssessment().getStatus()) {
            case Creating:
                return true;
            case Created:
                return !isCurrentUserEmployee();
            case EmployeeCompleted:
                return !isCurrentUserManager();
            case ManagerCompleted:
                return !isCurrentUserManager();
            case Reviewed:
                return true;
            default:
                return true;
        }
    }

    public void commit() throws ValidationException {
        this.binder.writeBean(this.rating);
    }

//    public void addDirtyListener(final AssessmentDirtyListener listener) {
//        if (listener != null) {
//            this.dirtyListeners.add(listener);
//        }
//    }

    private EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

    Registration addAssessmentDirtyListener(final AssessmentDirtyListener listener) {
        return getEventRouter().addListener(AssessmentDirtyEvent.class, listener, AssessmentDirtyListener.class.getDeclaredMethods()[0]);
    }

    Registration addAssessmentRecalculationListener(final AssessmentRecalculationListener listener) {
        return getEventRouter().addListener(AssessmentRecalculationEvent.class, listener, AssessmentRecalculationListener.class.getDeclaredMethods()[0]);
    }

    Registration addStatusChangeListener(final StatusChangeListener listener) {
        return getEventRouter().addListener(StatusChangeEvent.class, listener, StatusChangeListener.class.getDeclaredMethods()[0]);
    }

    @SuppressWarnings("unused")
    private void removeValueChangeListener() {
        if (this.valueChangeRegistration != null) {
            this.valueChangeRegistration.remove();
            this.valueChangeRegistration = null;
        }
    }

    private void registerValueChangeListener() {
        removeValueChangeListener();
        this.valueChangeRegistration = this.binder.addValueChangeListener(this::onValueChange);
    }

    private void onValueChange(final HasValue.ValueChangeEvent event) {

        boolean ratingChanged = ratingField.equals(event.getSource());
        boolean recalculationRequired = weightField.equals(event.getSource()) || ratingChanged;

        if (recalculationRequired) {
            this.scoreField.markAsDirty();
        }

        if (ratingChanged) {
            onRatingChange(ratingField.getValue());
        }

        getEventRouter().fireEvent(new AssessmentRecalculationEvent(this, 0, this.weightField.getValue(), this.ratingField.getValue()));
    }

    private void onRatingChange(Double rating) {
        switch(getValue().getAssessment().getStatus()) {
            case Created:
				/* Set the employee self rating */
                getValue().setEmployeeRating(rating);
                this.employeeRatingField.setValue(rating);
                //this.employeeRatingField.markAsDirty();
                break;
            case EmployeeCompleted:
				/* Set the manager rating */
                getValue().setManagerRating(rating);
                this.managerRatingField.setValue(rating);
                //this.managerRatingField.markAsDirty();
                break;
            case ManagerCompleted:
				/* Set the review rating */
                getValue().setReviewRating(rating);
                this.reviewRatingField.setValue(rating);
                //this.reviewRatingField.markAsDirty();
                break;
            default:
				/* Not set */
        }
    }
}