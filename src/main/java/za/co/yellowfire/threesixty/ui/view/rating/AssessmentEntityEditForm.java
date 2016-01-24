package za.co.yellowfire.threesixty.ui.view.rating;

import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.button.MButton;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;

import za.co.yellowfire.threesixty.domain.mail.MailingException;
import za.co.yellowfire.threesixty.domain.mail.SendGridMailingService;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentRatingsField.ChangeEvent;
import za.co.yellowfire.threesixty.ui.view.rating.AssessmentRatingsField.RecalculationEvent;

@SuppressWarnings("serial")
public class AssessmentEntityEditForm extends AbstractEntityEditForm<Assessment> {

	@PropertyId(Assessment.FIELD_MANAGER)
	private MComboBox managerField;
	@PropertyId(Assessment.FIELD_EMPLOYEE)
	private MComboBox employeeField;
	@PropertyId(Assessment.FIELD_PERIOD)
	private MComboBox periodField;
	@PropertyId(Assessment.FIELD_RATINGS)
	private AssessmentRatingsField ratingsField;
	
	private final SendGridMailingService mailingService;
	private AssessmentService service;
	private User currentUser;
	
	private MButton publishButton = new MButton("Publish").withIcon(FontAwesome.STEP_FORWARD).withListener(this::onPublish);
	private MButton employeeCompleteButton = new MButton("Employee Complete").withIcon(FontAwesome.STEP_FORWARD).withListener(this::onEmployeeComplete);
	private MButton managerCompleteButton = new MButton("Manager Complete").withIcon(FontAwesome.STEP_FORWARD).withListener(this::onManagerComplete);
	private MButton concludeButton = new MButton("Conclude").withIcon(FontAwesome.STEP_FORWARD).withListener(this::onConclude);
	
	public AssessmentEntityEditForm(AssessmentService service, final SendGridMailingService mailingService, final User currentUser) {
		this.service = service;
		this.mailingService = mailingService;
		this.currentUser = currentUser;
		
		this.managerField = 
				new MComboBox(I8n.Assessment.Fields.MANAGER, new IndexedContainer(service.findActiveUsers()))
					.withWidth(100.0f, Unit.PERCENTAGE)
					.withDisabled();
		
		this.employeeField = 
				new MComboBox(I8n.Assessment.Fields.EMPLOYEE, new IndexedContainer(service.findActiveUsers()))
					.withWidth(100.0f, Unit.PERCENTAGE)
					.valueChangeListener(this::onEmployeeSelected);
		
		this.periodField =
				new MComboBox(I8n.Assessment.Fields.PERIOD, new IndexedContainer(service.findActivePeriods()))
					.withWidth(100.0f, Unit.PERCENTAGE);
		
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
		this.ratingsField.setAssessmentStatus(getValue().getStatus());
		this.ratingsField.setPossibleRatings(service.findPossibleRatings());
		this.ratingsField.setPossibleWeightings(service.findPossibleWeightings());
		
		addComponent(PanelBuilder.FORM(
        		PanelBuilder.HORIZONTAL(idField, employeeField, managerField, periodField),
        		ratingsField
        		));
		
		maintainAssessment();
	}
	
	@Override
	protected Assessment buildEmpty() {
		return Assessment.EMPTY();
	}
	
	protected void onValueChange(final ValueChangeEvent event) {
		super.onValueChange(event);
		
		if (isModified()) {
			showButtons();
			enableButtons();
		}
	}
	
	public void onAssessmentRatingChange(final ChangeEvent event) {
		
	}
	
	public void onRecalculation(final RecalculationEvent event) {
		
		showButtons();
		enableButtons();
	}
	
	public void onPublish(final ClickEvent event) {
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
	
	public void onEmployeeComplete(final ClickEvent event) {
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
	
	public void onManagerComplete(final ClickEvent event) {
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

	public void onConclude(final ClickEvent event) {
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

	public void progressAssessment() {
		try {
			//Commit the assessment and progress the status
	        commit();
	        getValue().calculate();
	        getValue().progressStatus();
	        //Persist the assessment
	        service.save(getValue(), currentUser);
	        //Notify the user of the outcome
	        String successNotification;
			switch (getValue().getStatus()) {
				case Created: successNotification = "Assessment published for employee self assessment"; break;
				case EmployeeCompleted: successNotification = "Employee self assessment completed"; break;
				case ManagerCompleted: successNotification = "Management assessment of employee completed"; break;
				case Reviewed: successNotification = "Assessment review concluded"; break;
				default: successNotification = "The assessment has been successsfully saved";
			}
			NotificationBuilder.showNotification("Assessment progression", successNotification);
	        //Update the user interface
	        maintainAssessment();
	        //Inform the view that the form is clean
	        fireFormClean();
		} catch (CommitException exception) {
            Notification.show("Error while updating : " + exception.getMessage(), Type.ERROR_MESSAGE);
        }
	}
	
	public void maintainAssessment() {
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
	public void showButtons() {
		this.publishButton.setVisible(getValue().getStatus() == AssessmentStatus.Creating);
		this.employeeCompleteButton.setVisible(getValue().getStatus() == AssessmentStatus.Created);
		this.managerCompleteButton.setVisible(getValue().getStatus() == AssessmentStatus.EmployeeCompleted);
		this.concludeButton.setVisible(getValue().getStatus() == AssessmentStatus.ManagerCompleted);
	}
	
	/**
	 * Enable the buttons according to the status and the completeness
	 */
	public void enableButtons() {
		this.publishButton.setEnabled(canEnablePublish());
		this.employeeCompleteButton.setEnabled(canEnableEmployeeComplete());
		this.managerCompleteButton.setEnabled(canEnableManagerComplete());
		this.concludeButton.setEnabled(canEnableConclude());
	}
	
	public boolean canEnablePublish() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.Creating && 
				getValue().getWeightingTotal() == 100.0;
	}
	
	public boolean canEnableEmployeeComplete() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.Created && 
				getValue().getEmployee().equals(currentUser) &&
				getValue().isComplete();
	}
	
	public boolean canEnableManagerComplete() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.EmployeeCompleted && 
				getValue().getManager().equals(currentUser) &&
				getValue().isComplete();
	}
	
	public boolean canEnableConclude() {
		return isValid() && 
				getValue().getStatus() == AssessmentStatus.ManagerCompleted && 
				getValue().getManager().equals(currentUser) &&
				getValue().isComplete();
	}
	
	public boolean isReadonlyRatings() {
		return !canEnableRatings();
	}
	
	public boolean canEnableRatings() {
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
	
	public boolean canEnabledEmployee() {
		return getValue().getStatus() == AssessmentStatus.Creating && this.currentUser.isAdmin();
	}
	
	public boolean isEmployeeReadOnly() {
		return !canEnabledEmployee();
	}
	
	public boolean isManagerReadOnly() {
		return !canEnabledEmployee();
	}
	
	public boolean canEnablePeriod() {
		return getValue().getStatus() == AssessmentStatus.Creating && this.currentUser.isAdmin();
	}
	
	public boolean isPeriodReadOnly() {
		return !canEnablePeriod();
	}
	
	public void onEmployeeSelected(Property.ValueChangeEvent event) {
		User user = (User) event.getProperty().getValue();
		if (user != null && user.getReportsTo() != null) {
			this.managerField.select(user.getReportsTo());
			this.managerField.markAsDirty();
		} else {
			this.managerField.select(null);
			this.managerField.markAsDirty();
		}
		/* Commit the employee and manager fields so that the ratings security can be determined */
		this.employeeField.commit();
		this.managerField.commit();
		
		/*Fire the change */
		this.ratingsField.fireAssessmentParticipantsChanged();
	}
}
