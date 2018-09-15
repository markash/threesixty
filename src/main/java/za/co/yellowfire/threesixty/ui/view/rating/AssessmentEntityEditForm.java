package za.co.yellowfire.threesixty.ui.view.rating;

import com.github.markash.ui.component.notification.NotificationBuilder;
import com.github.markash.ui.security.CurrentUserProvider;
import com.github.markash.ui.view.AbstractEntityEditForm;
import com.vaadin.data.HasValue;
import com.vaadin.data.StatusChangeEvent;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.*;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.view.period.PeriodModel;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@SuppressWarnings("serial")
public class AssessmentEntityEditForm extends AbstractEntityEditForm<Assessment> {
    /* Assessment */
	private ComboBox<User> managerField;
	private ComboBox<User> employeeField;
	private ComboBox<PeriodModel> periodField;
    /* Summary */
    private AssessmentSummaryHeader summary;

    /* Ratings */
    private AssessmentRatingsField ratingsField;

	private final ListDataProvider<PeriodModel> periodListDataProvider;

	private AssessmentService service;
	private User currentUser;

	private MButton addButton = new MButton("Add").withIcon(VaadinIcons.PLUS_CIRCLE).withListener(this::onAdd);
	private MButton publishButton = new MButton("Publish").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onPublish);
	private MButton employeeCompleteButton = new MButton("Employee Complete").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onEmployeeComplete);
	private MButton managerCompleteButton = new MButton("Manager Complete").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onManagerComplete);
	private MButton concludeButton = new MButton("Conclude").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onConclude);
	
	AssessmentEntityEditForm(
            final ListDataProvider<PeriodModel> periodListDataProvider,
            final ListDataProvider<User> userListDataProvider,
            final AssessmentService service,
            final CurrentUserProvider<User> currentUserProvider) {

	    super(Assessment.class);

	    this.service = service;
		currentUserProvider.get().ifPresent(p -> this.currentUser = p);

		this.periodListDataProvider = periodListDataProvider;
		this.managerField = new ComboBox<>(I8n.Assessment.Fields.MANAGER);
        this.managerField.setWidth(100.0f, Unit.PERCENTAGE);
        this.managerField.setDataProvider(userListDataProvider);
        this.managerField.setEnabled(false);
		
		this.employeeField = new ComboBox<>(I8n.Assessment.Fields.EMPLOYEE);
		this.employeeField.setWidth(100.0f, Unit.PERCENTAGE);
		this.employeeField.setDataProvider(userListDataProvider);
		this.employeeField.addValueChangeListener(this::onEmployeeSelected);
		
		this.periodField = new ComboBox<>(I8n.Assessment.Fields.PERIOD);
		this.periodField.setWidth(100.0f, Unit.PERCENTAGE);
		this.periodField.setDataProvider(this.periodListDataProvider);

        this.summary = new AssessmentSummaryHeader(addButton, publishButton, employeeCompleteButton, managerCompleteButton, concludeButton);

		this.ratingsField = 
				new AssessmentRatingsField(
						service.findPossibleRatings(), 
						service.findPossibleWeightings(),
						service.findDisciplines());
			
		this.ratingsField.setCurrentUser(currentUser);
		this.ratingsField.addAssessmentRecalculationListener(this::onAssessmentRecalculation);
		this.ratingsField.addStatusChangeListener(this::onStatusChange);

		//this.ratingsField.addAssessmentRatingListener(this::onAssessmentRatingChange);
		//this.ratingsField.addRecalculationListener(this::onRecalculation);

		getBinder().forField(managerField).bind(Assessment.FIELD_MANAGER);
        getBinder().forField(employeeField).bind(Assessment.FIELD_EMPLOYEE);
        getBinder().forField(periodField).withConverter(PeriodModel.converter()).bind(Assessment.FIELD_PERIOD);
        getBinder().forField(ratingsField).bind(Assessment.FIELD_RATINGS);
	}
	
	@Override
	protected void internalLayout() {
//		this.ratingsField.setValue(getValue());
//		this.ratingsField.setAssessmentStatus(getValue().getStatus());
//		this.ratingsField.setPossibleRatings(service.findPossibleRatings());
//		this.ratingsField.setPossibleWeightings(service.findPossibleWeightings());

        /* Create the header with the header buttons */
        Label headerCaption = new MLabel(VaadinIcons.BARCODE.getHtml() + I8n.Assessment.Rating.PLURAL)
                .withContentMode(ContentMode.HTML)
                .withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);

        MHorizontalLayout header = new MHorizontalLayout(headerCaption, this.summary).withStyleName(Style.AssessmentRating.HEADER);
        header.setExpandRatio(headerCaption, 1.0f);
        header.setExpandRatio(this.summary, 2.0f);
        header.withMargin(false);

        addComponents(
                new MVerticalLayout(
                        new MHorizontalLayout(getIdField(), employeeField, managerField, periodField).withFullWidth(),
                        new MVerticalLayout(header, ratingsField).withMargin(false)
                ).withMargin(false)
        );

		//maintainAssessment();
		restrictAvailablePeriodsForEmployee();
	}

//	protected void onValueChange(final HasValue.ValueChangeEvent event) {
//		super.onValueChange(event);
//
//		if (isModified()) {
//			showButtons();
//			enableButtons();
//		}
//	}
	
//	private void onAssessmentRatingChange(final AssessmentRatingEvent event) {
//	}

	//DELETE
//	private void onRecalculation(final AssessmentRecalculationEvent event) {
//		showButtons();
//		enableButtons();
//	}

	/**
	 * Whether the form is valid or has validation warnings. The current binder and the ratings field validation status
	 * is taken into consideration.
	 * @return True if valid else false
	 */
	public boolean isValid() {
		return super.isValid() && this.ratingsField.isValid();
	}

	/**
	 * Whether the form has changes. The current binder and the ratings field changes are taken into consideration.
	 * @return True if the form has changes else false
	 */
	@Override
	public boolean isModified() {
		return super.isModified() || this.ratingsField.hasChanges();
	}

    /**
     * Commits the binder and the underlying ratings to the bound values
     * @throws ValidationException If there was a validation error
     */
    @Override
    public void commit() throws ValidationException {
        super.commit();
        this.ratingsField.commit();
    }

    /**
     * Provide a hook for subclasses to update dependant fields
     */
    @Override
    protected void updateDependentFields() {
        final Optional<Assessment> assessment = Optional.ofNullable(getValue());
        if (assessment.isPresent()) {
            maintainAssessment();
        }
    }

    private void onStatusChange(final StatusChangeEvent event) {
        /* Bubble the status change event up to the listeners of the form (i.e. edit view) */
        getEventRouter().fireEvent(event);
    }

    private void onAssessmentRecalculation(final AssessmentRecalculationEvent event) {
        /* Fire the assessment summary */
        this.summary.recalculate(event);
        /* Show & enable the buttons */
		showButtons();
		enableButtons();
    }

    @SuppressWarnings("unused")
	private void onPublish(final ClickEvent event) {
        progressAssessmentConfirmation(I8n.Assessment.Confirmation.PUBLISH);
	}

    @SuppressWarnings("unused")
	private void onEmployeeComplete(final ClickEvent event) {
        progressAssessmentConfirmation(I8n.Assessment.Confirmation.EMPLOYEE_COMPLETE);
	}

	@SuppressWarnings("unused")
	private void onManagerComplete(final ClickEvent event) {
        progressAssessmentConfirmation(I8n.Assessment.Confirmation.MANAGER_COMPLETE);
	}

    @SuppressWarnings("unused")
	private void onConclude(final ClickEvent event) {
        progressAssessmentConfirmation(I8n.Assessment.Confirmation.REVIEW_COMPLETE);
	}

	private void progressAssessmentConfirmation(final String message) {
        ConfirmDialog.show(
                UI.getCurrent(),
                I8n.Confirmation.TITLE,
                message,
                I8n.Confirmation.YES,
                I8n.Confirmation.NO,
                dialog -> { if (dialog.isConfirmed()) progressAssessment(); });
    }

	private void progressAssessment() {
		try {
			/* Commit the assessment and progress the status */
	        commit();
	        getValue().calculate();
	        getValue().progressStatus();
	        /* Persist the assessment */
	        service.save(getValue());
	        /* Notify the user of the outcome */
	        String successNotification;
			switch (getValue().getStatus()) {
				case Created:
				    successNotification = "Assessment published for employee self assessment";
				    break;
				case EmployeeCompleted:
				    successNotification = "Employee self assessment completed";
				    break;
				case ManagerCompleted:
				    successNotification = "Management assessment of employee completed";
				    break;
				case Reviewed:
				    successNotification = "Assessment review concluded";
				    break;
				default: successNotification = "The assessment has been successfully saved";
			}
			NotificationBuilder.showNotification("Assessment progression", successNotification);
	        //Update the user interface
	        maintainAssessment();
		} catch (Exception exception) {
            Notification.show("Error while updating : " + exception.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
	}
	
	private void maintainAssessment() {

	    /* If the assessment is creating and no ratings then create the first rating */
	    if (getValue().isBlank()) {
	        getValue().addAssessmentRating();
        }

		this.employeeField.setReadOnly(isEmployeeReadOnly());
		this.managerField.setReadOnly(isManagerReadOnly());
		this.periodField.setReadOnly(isPeriodReadOnly());
		
		//The manager field must either be read-only or disabled
		if (!managerField.isReadOnly()) {
			managerField.setEnabled(false);
		}

        this.summary.setAssessment(getValue());
        //this.ratingsField.setValue(getValue());
		//this.ratingsField.setAssessmentStatus(getValue().getStatus());
		this.ratingsField.setCurrentUser(currentUser);
		
		showButtons();
		enableButtons();
	}

	/**
	 * Show buttons according to the status
	 */
	private void showButtons() {
		this.publishButton.setVisible(getValue().getStatus() == AssessmentStatus.Creating);
		this.employeeCompleteButton.setVisible(getValue().getStatus() == AssessmentStatus.Created);
		this.managerCompleteButton.setVisible(getValue().getStatus() == AssessmentStatus.EmployeeCompleted);
		this.concludeButton.setVisible(getValue().getStatus() == AssessmentStatus.ManagerCompleted);
	}
	
	/**
	 * Enable the buttons according to the status and the completeness
	 */
	private void enableButtons() {
        this.addButton.setEnabled(isAddButtonEnabled());
		this.publishButton.setEnabled(canEnablePublish());
		this.employeeCompleteButton.setEnabled(canEnableEmployeeComplete());
		this.managerCompleteButton.setEnabled(canEnableManagerComplete());
		this.concludeButton.setEnabled(canEnableConclude());
	}

    private boolean isAddButtonEnabled() {
        return getValue() != null && getValue().getStatus().isEditingAllowed() && getValue().hasParticipants();
    }

	private boolean canEnablePublish() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.Creating && 
				getValue().getWeightingTotal() == 100.0;
	}
	
	private boolean canEnableEmployeeComplete() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.Created && 
				getValue().getEmployee().equals(currentUser) &&
				getValue().isComplete();
	}
	
	private boolean canEnableManagerComplete() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.EmployeeCompleted && 
				getValue().getManager().equals(currentUser) &&
				getValue().isComplete();
	}
	
	private boolean canEnableConclude() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.ManagerCompleted && 
				getValue().getManager().equals(currentUser) &&
				getValue().isComplete();
	}
	
	public boolean isReadonlyRatings() {
		return !canEnableRatings();
	}
	
	private boolean canEnableRatings() {
		switch (getValue().getStatus()) {
		case Creating:
			return true;
		case Created:
			return getValue().getEmployee().equals(currentUser);
		case EmployeeCompleted:
			return getValue().getManager().equals(currentUser);
		case ManagerCompleted:
			return getValue().getManager().equals(currentUser);
		case Reviewed:
			return false;
		default:
			return false;
		}
	}
	
	private boolean canEnabledEmployee() {
		return isStatusInCreating() && isCurrentUserAdmin();
	}
	
	private boolean isEmployeeReadOnly() {
		return !canEnabledEmployee();
	}
	
	private boolean isManagerReadOnly() {
		return !canEnabledEmployee();
	}
	
	private boolean canEnablePeriod() {
		return isStatusInCreating() && isCurrentUserAdmin();
	}
	
	private boolean isPeriodReadOnly() {
		return !canEnablePeriod();
	}

	private boolean isStatusInCreating() {
	    return getValue().getStatus() == AssessmentStatus.Creating;
    }

	private boolean isCurrentUserAdmin() {
	    return Optional.ofNullable(this.currentUser).map(User::isAdmin).orElse(false);
    }

    private void onAdd(final ClickEvent event) {
        this.ratingsField.add(getValue().addAssessmentRating(), true);
    }

    private void fireAssessmentRatingAdded(final AssessmentRating rating) {
        //getEventRouter().fireEvent(new AssessmentRatingEvent(this, rating, AssessmentRatingEvent.Action.ADD));
    }

	private void onEmployeeSelected(HasValue.ValueChangeEvent<User> event) {
		User user = event.getValue();
		if (user != null && user.getReportsTo() != null) {
			this.managerField.setSelectedItem(user.getReportsTo());
			this.managerField.markAsDirty();
		} else {
			this.managerField.setSelectedItem(null);
			this.managerField.markAsDirty();
		}
		/* Commit the employee and manager fields so that the ratings security can be determined */
		//this.employeeField.commit();
		//this.managerField.commit();
		
		/* Set the available period available for the employee assessment */
		refreshAvailablePeriodsForEmployee();
		
		/*Fire the change */
		//this.ratingsField.refresh();
	}
	
	private void restrictAvailablePeriodsForEmployee() {
		if (getValue() != null) {
			final Set<Period> assessedPeriods = new HashSet<>(service.findAssessmentPeriodsForEmployee(getValue().getEmployee(), getValue()));
            SerializablePredicate<PeriodModel> unassessedPeriodsFunction = periodModel -> assessedPeriods.contains(periodModel.getWrapped());
            this.periodListDataProvider.setFilter(unassessedPeriodsFunction);
		}
	}
	
	private void refreshAvailablePeriodsForEmployee() {
		if (getValue() != null) {
			this.periodField.getDataProvider().refreshAll();
		}
	}
}
