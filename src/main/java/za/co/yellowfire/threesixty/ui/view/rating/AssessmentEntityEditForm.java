package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.data.HasValue;
import com.vaadin.data.ValidationException;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.SerializablePredicate;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import io.threesixty.ui.component.notification.NotificationBuilder;
import io.threesixty.ui.component.panel.PanelBuilder;
import io.threesixty.ui.security.CurrentUserProvider;
import io.threesixty.ui.view.AbstractEntityEditForm;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.domain.PersistenceException;
import za.co.yellowfire.threesixty.domain.mail.MailingException;
import za.co.yellowfire.threesixty.domain.mail.SendGridMailingService;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.period.PeriodModel;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentRatingsField.ChangeEvent;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentRatingsField.RecalculationEvent;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class AssessmentEntityEditForm extends AbstractEntityEditForm<Assessment> {

	private ComboBox<User> managerField;
	private ComboBox<User> employeeField;
	private ComboBox<za.co.yellowfire.threesixty.ui.view.period.PeriodModel> periodField;
	private AssessmentRatingsField ratingsField;

	private final ListDataProvider<PeriodModel> periodListDataProvider;

	private final SendGridMailingService mailingService;
	private AssessmentService service;
	private User currentUser;
	
	private MButton publishButton = new MButton("Publish").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onPublish);
	private MButton employeeCompleteButton = new MButton("Employee Complete").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onEmployeeComplete);
	private MButton managerCompleteButton = new MButton("Manager Complete").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onManagerComplete);
	private MButton concludeButton = new MButton("Conclude").withIcon(VaadinIcons.STEP_FORWARD).withListener(this::onConclude);
	
	AssessmentEntityEditForm(
            final ListDataProvider<PeriodModel> periodListDataProvider,
            final ListDataProvider<User> userListDataProvider,
            final AssessmentService service,
            final SendGridMailingService mailingService,
            final CurrentUserProvider<User> currentUserProvider) {

	    super(Assessment.class);

	    this.service = service;
		this.mailingService = mailingService;
		this.currentUser = currentUser;

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

		this.ratingsField = 
				new AssessmentRatingsField(
						service.findPossibleRatings(), 
						service.findPossibleWeightings(),
						service.findPerformanceAreas(),
						new MButton[] {publishButton, employeeCompleteButton, managerCompleteButton, concludeButton});
			
		this.ratingsField.setCurrentUser(currentUser);
		this.ratingsField.setAssessmentRatingListener(this::onAssessmentRatingChange);
		this.ratingsField.setRecalculationListener(this::onRecalculation);
	}
	
	@Override
	protected void internalLayout() {
		this.ratingsField.setAssessment(getValue());
//		this.ratingsField.setAssessmentStatus(getValue().getStatus());
//		this.ratingsField.setPossibleRatings(service.findPossibleRatings());
//		this.ratingsField.setPossibleWeightings(service.findPossibleWeightings());
		
		addComponent(PanelBuilder.FORM(
        		PanelBuilder.HORIZONTAL(getIdField(), employeeField, managerField, periodField),
        		ratingsField
        		));
		
//		maintainAssessment();
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
	
	private void onAssessmentRatingChange(final ChangeEvent event) {
	}
	
	private void onRecalculation(final RecalculationEvent event) {
		showButtons();
		enableButtons();
	}
	
	private void onPublish(final ClickEvent event) {
		ConfirmDialog.show(
				UI.getCurrent(), 
				"Confirmation", 
				"Publishing the assessment will inform the employee that the assessment is ready for review " +
				"and further changes are not possible.\nWould you like to publish the assessment?",
				"Yes",
				"No",
		        new ConfirmDialog.Listener() {
		            public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	progressAssessment();
		                	try {
		                	mailingService.send();
		                	} catch (MailingException e) {
		                		e.printStackTrace();
		                	}
		                }
		            }
				});
	}
	
	private void onEmployeeComplete(final ClickEvent event) {
		ConfirmDialog.show(
				UI.getCurrent(), 
				"Confirmation", 
				"Completing the self-review will inform the manager that the assessment is ready for review " +
				"and further changes are not possible.\nWould you like to proceed?",
				"Yes",
				"No",
		        new ConfirmDialog.Listener() {
		            public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	progressAssessment();
		                }
		            }
				});
	}
	
	private void onManagerComplete(final ClickEvent event) {
		ConfirmDialog.show(
				UI.getCurrent(), 
				"Confirmation", 
				"Completing the manager review will inform the employee that the assessment is ready for final review " +
				"and further changes are not possible.\nWould you like to proceed?",
				"Yes",
				"No",
		        new ConfirmDialog.Listener() {
		            public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	progressAssessment();
		                }
		            }
				});
	}

	private void onConclude(final ClickEvent event) {
		ConfirmDialog.show(
				UI.getCurrent(), 
				"Confirmation", 
				"Completing the final review will close off the assessment " +
				"and further changes are not possible.\nWould you like to proceed?",
				"Yes",
				"No",
		        new ConfirmDialog.Listener() {
		            public void onClose(ConfirmDialog dialog) {
		                if (dialog.isConfirmed()) {
		                	progressAssessment();
		                }
		            }
				});
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
				case Created: successNotification = "Assessment published for employee self assessment"; break;
				case EmployeeCompleted: successNotification = "Employee self assessment completed"; break;
				case ManagerCompleted: successNotification = "Management assessment of employee completed"; break;
				case Reviewed: successNotification = "Assessment review concluded"; break;
				default: successNotification = "The assessment has been successfully saved";
			}
			NotificationBuilder.showNotification("Assessment progression", successNotification);
	        //Update the user interface
	        maintainAssessment();
		} catch (PersistenceException | ValidationException exception) {
            Notification.show("Error while updating : " + exception.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
	}
	
	private void maintainAssessment() {
		this.employeeField.setReadOnly(isEmployeeReadOnly());
		this.managerField.setReadOnly(isManagerReadOnly());
		this.periodField.setReadOnly(isPeriodReadOnly());
		
		//The manager field must either be read-only or disabled
		if (!managerField.isReadOnly()) {
			managerField.setEnabled(false);
		}
		
		this.ratingsField.setAssessmentStatus(getValue().getStatus());
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
		this.publishButton.setEnabled(canEnablePublish());
		this.employeeCompleteButton.setEnabled(canEnableEmployeeComplete());
		this.managerCompleteButton.setEnabled(canEnableManagerComplete());
		this.concludeButton.setEnabled(canEnableConclude());
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
		return getValue().getStatus() == AssessmentStatus.Creating && this.currentUser.isAdmin();
	}
	
	private boolean isEmployeeReadOnly() {
		return !canEnabledEmployee();
	}
	
	private boolean isManagerReadOnly() {
		return !canEnabledEmployee();
	}
	
	private boolean canEnablePeriod() {
		return getValue().getStatus() == AssessmentStatus.Creating && this.currentUser.isAdmin();
	}
	
	private boolean isPeriodReadOnly() {
		return !canEnablePeriod();
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
		this.ratingsField.fireAssessmentParticipantsChanged();
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
