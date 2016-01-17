package za.co.yellowfire.threesixty.ui.view.rating;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.apache.commons.lang3.ArrayUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentRating;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.LabelBuilder;
import za.co.yellowfire.threesixty.ui.component.PanelBuilder;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.DirtyEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.DirtyListener;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.FormDirtyEvent;

@SuppressWarnings("serial")
public class AssessmentRatingsField extends CustomField<Set<AssessmentRating>> {	
	/* Tabs and panels */
	private TabSheet tabSheet;
	private LinkedList<AssessmentRatingPanel> panels = new LinkedList<>();
	
	/* Header */
	private AssessmentSummaryHeader summary;
	private MButton addButton = new MButton("Add").withIcon(FontAwesome.PLUS_CIRCLE).withListener(this::onAdd);
	
	/* Fields */
	private LinkedList<Double> possibleRatings = new LinkedList<>();
	private LinkedList<Double> possibleWeightings = new LinkedList<>();
	private LinkedList<PerformanceArea> performanceAreas = new LinkedList<>();
	
	/* Listeners */
	private AssessmentRatingListener listener;
	private RecalculationListener recalculationListener;
	
	private User currentUser;
	private Assessment assessment;
	private AssessmentStatus assessmentStatus = AssessmentStatus.Creating;
	
	public AssessmentRatingsField(
			final Collection<Double> possibleRatings, 
			final Collection<Double> possibleWeightings,
			final Collection<PerformanceArea> performanceAreas,
			final Button[] headerButtons) {
		this(possibleRatings, possibleWeightings, performanceAreas, null, null, headerButtons);
	}
	
	public AssessmentRatingsField(
			final Collection<Double> possibleRatings, 
			final Collection<Double> possibleWeightings,
			final Collection<PerformanceArea> performanceAreas,
			final AssessmentRatingListener listener,
			final RecalculationListener recalculationListener,
			final Button[] buttons) {
		
		this.possibleRatings.addAll(possibleRatings);
		this.possibleWeightings.addAll(possibleWeightings);
		this.performanceAreas.addAll(performanceAreas);
		
		this.listener = listener;
		this.recalculationListener = recalculationListener;
		this.summary = new AssessmentSummaryHeader(ArrayUtils.addAll(new Button[] {addButton}, buttons));
	}
	
	public void setAssessmentRatingListener(final AssessmentRatingListener listener) {
		this.listener = listener;
	}
	
	public AssessmentRatingListener getAssessmentRatingListener() {
		return this.listener;
	}
	
	public void setRecalculationListener(final RecalculationListener listener) {
		this.recalculationListener = listener;
	}
	
	public RecalculationListener getRecalculationListener() {
		return this.recalculationListener;
	}
	
	@Override
	protected Component initContent() {
				
		this.tabSheet = new TabSheet();
		this.setWidth(100.0f, Unit.PERCENTAGE);
		Responsive.makeResponsive(this.tabSheet);
		
		if (getValue() != null) {
			
			int i = 0;
			for(AssessmentRating rating : getValue()) {
				AssessmentRatingPanel panel = addAssessmentRatingPanel(rating, currentUser);
				panel.setCurrentUser(currentUser);
				
				if (i++ == 0) {
					this.summary.setAssessment(rating.getAssessment());
				}
			}
		} else {
			addAssessmentRating();
		}
		
		/* Create the header with the header buttons */
		Label headerCaption = 
				LabelBuilder.build(FontAwesome.BARCODE.getHtml() +  " Assessent ratings", 
						ContentMode.HTML, 
						ValoTheme.LABEL_H3, 
						ValoTheme.LABEL_NO_MARGIN);
		
		HorizontalLayout header = PanelBuilder.HORIZONTAL(Style.AssessmentRating.HEADER, 
				headerCaption,
				this.summary);
		header.setExpandRatio(headerCaption, 1.0f);
		header.setExpandRatio(this.summary, 2.0f);
		
		/* Vertically layout the header and tab sheet */
		return PanelBuilder.VERTICAL(header, tabSheet);
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<Set<AssessmentRating>> getType() {
		Set<AssessmentRating> test = new HashSet<AssessmentRating>();
		return (Class<Set<AssessmentRating>>) test.getClass();
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		
		for(AssessmentRatingPanel panel : this.panels) {
			panel.commit();
		}
		
		super.commit();
	}

	@Override
	protected void setInternalValue(final Set<AssessmentRating> newValue) {
		super.setInternalValue(newValue);
		
		/* Maintain the tab sheet and panels */
		if (this.tabSheet != null) {
			for (AssessmentRatingPanel panel : this.panels) {
				this.tabSheet.removeTab(this.tabSheet.getTab(panel));
			}
			panels.clear();
			
			if (getValue() != null) {
				for(AssessmentRating rating : getValue()) {
					addAssessmentRatingPanel(rating, currentUser);
				}
			} else {
				addAssessmentRating();
			}
		}
	}

	protected boolean isAddButtonEnabled() {
		return this.assessmentStatus.isEditingAllowed() && this.assessment != null && this.assessment.hasParticipants();
	}
	
	public void setCurrentUser(final User currentUser) {
		this.currentUser = currentUser;
		this.addButton.setEnabled(isAddButtonEnabled());
		
		for(AssessmentRatingPanel panel : this.panels) {
			panel.setCurrentUser(currentUser);
		}
	}

	public void setAssessment(final Assessment assessment) {
		this.assessment = assessment; 
		this.summary.setAssessment(assessment);
	}

	public void setAssessmentStatus(final AssessmentStatus status) {
		this.assessmentStatus = status;
		this.addButton.setEnabled(isAddButtonEnabled());
		
		for(AssessmentRatingPanel panel : this.panels) {
			panel.updateFieldAccess();
		}
	}
	
	public void fireAssessmentParticipantsChanged() {
		this.addButton.setEnabled(isAddButtonEnabled());
	}
	
	public void setPossibleWeightings(final Collection<Double> weightings) {
		for(AssessmentRatingPanel panel : this.panels) {
			panel.setPossibleWeightings(weightings);
		}
	}
	
	public void setPossibleRatings(final Collection<Double> ratings) {
		for(AssessmentRatingPanel panel : this.panels) {
			panel.setPossibleRatings(ratings);
		}
	}
	
	public void addAssessmentRating() {
		AssessmentRating rating = new AssessmentRating();
		rating.setAssessment(assessment);
		
		getValue().add(rating);
		
		AssessmentRatingPanel panel = addAssessmentRatingPanel(rating, currentUser);
		this.tabSheet.setSelectedTab(panel);
		
		fireAssessmentRatingAdded(rating);
	}
	
	protected AssessmentRatingPanel addAssessmentRatingPanel(final AssessmentRating rating, final User currentUser) {
		
		/* Add the the new assessment panel */
		AssessmentRatingPanel panel = new AssessmentRatingPanel(rating, currentUser, this.possibleRatings, this.possibleWeightings, this.performanceAreas);
		
		Tab tab = this.tabSheet.addTab(panel);
		tab.setCaption("Rating #" + (tabSheet.getTabPosition(tab) + 1));
		panels.add(panel);
						
		panel.addDirtyListener(this::onFormDirty);
		
		return panel;
	}
	
	protected void fireAssessmentRatingAdded(final AssessmentRating rating) {
		if (this.listener != null) {
			this.listener.onChange(new ChangeEvent(rating));
		}
	}
	
	protected void fireRecalculationRequired() {
	
		/* Recalculate the summary values */
		this.summary.recalculate();
		
		/* Fire the recalculation event */
		if (this.recalculationListener != null) {	
			this.recalculationListener.onRecalc(
					new RecalculationEvent(
						this.summary.getNoOfRatings(), 
						this.summary.getWeightingTotal(), 
						this.summary.getScoreTotal()));
		}
	}

	protected void onFormDirty(final DirtyEvent event) {
		getState().modified = true;
		fireValueChange(false);
		
		if (event.isRecalculationRequired()) {
			fireRecalculationRequired();
		}
	}
	
	public void onAdd(final ClickEvent event) {
		this.addAssessmentRating();
	}
	
	private static class AssessmentRatingPanel extends GridLayout {
		@PropertyId("id")
		private MTextField idField = new MTextField("Id").withReadOnly(true);
		@PropertyId("performanceArea")
		private MComboBox areaField;
		
		@PropertyId("measurement")
		private MTextArea measurementField = 
			new MTextArea("Measurement")
				.withRows(15);
		@PropertyId("managerComment")
		private MTextArea managerCommentField = 
			new MTextArea("Manager Comment")
				.withRows(5);
		@PropertyId("employeeComment")
		private MTextArea employeeCommentField = 
			new MTextArea("Employee Comment")
				.withRows(5);
		@PropertyId("weight")
		private MComboBox weightField = 
			new MComboBox("Weight")
				.withWidth(100.0f, Unit.PERCENTAGE);
		@PropertyId("rating")
		private MComboBox ratingField = 
			new MComboBox("Rating")
				.withWidth(100.0f, Unit.PERCENTAGE);
		
		@PropertyId("score")
		private MTextField scoreField = 
			new MTextField("Score")
				.withWidth(100.0f, Unit.PERCENTAGE)
				.withReadOnly(true);
		
		/* Read only rating panel */
		private VerticalLayout ratingPanel;
		private Label employeeRatingField;
		private Label managerRatingField;
		private Label reviewRatingField;
		private HorizontalLayout employeeRatingPanel;
		private HorizontalLayout managerRatingPanel;
		private HorizontalLayout reviewRatingPanel;
		
		private User currentUser;
		private BeanItem<AssessmentRating> readOnlyFieldGroup;
		private BeanFieldGroup<AssessmentRating> fieldGroup;
		private LinkedHashSet<DirtyListener> dirtyListeners = new LinkedHashSet<>();
		
		public AssessmentRatingPanel(
				final AssessmentRating rating, 
				final User currentUser,
				final Collection<Double> possibleRatings, 
				final Collection<Double> possibleWeightings,
				final Collection<PerformanceArea> performanceAreas) {
			super(3, 3);
						
			setWidth(100.0f, Unit.PERCENTAGE);
			setSpacing(true);
			
			 this.areaField = 
						new MComboBox("Performance Area", new IndexedContainer(performanceAreas))
							.withWidth(100.0f, Unit.PERCENTAGE);
			 
			/* Set buffered to false so that the assessment score can be calculated on property change*/
			this.fieldGroup = BeanBinder.bind(rating, this, false);
			this.ratingField.addItems(possibleRatings);
			this.weightField.addItems(possibleWeightings);
			this.currentUser = currentUser;
			/* Setup read only ratings panel */
			this.readOnlyFieldGroup = new BeanItem<>(rating);
			this.employeeRatingField = new Label(this.readOnlyFieldGroup.getItemProperty("employeeRating"));
			this.managerRatingField = new Label(this.readOnlyFieldGroup.getItemProperty("managerRating"));
			this.reviewRatingField = new Label(this.readOnlyFieldGroup.getItemProperty("reviewRating"));
			
			/* Column 0 */
			addComponent(areaField, 0, 0);
			addComponent(managerCommentField, 0, 1);
			addComponent(employeeCommentField, 0, 2);
			
			/* Column 1 */
			addComponent(measurementField, 1, 0, 1, 2);
			
			/* Column 2 */
			Label ratingLabel = LabelBuilder.build("Self-rating: ", Style.Text.BOLDED);
			this.employeeRatingPanel = 
					PanelBuilder.HORIZONTAL(
							"rating-summary-item", 
							ratingLabel, 
							this.employeeRatingField);
			this.employeeRatingPanel.setExpandRatio(ratingLabel, 3);
			this.employeeRatingPanel.setExpandRatio(this.employeeRatingField, 1);
			this.employeeRatingPanel.setSpacing(true);
			
			ratingLabel = LabelBuilder.build("Manager rating: ", Style.Text.BOLDED);
			this.managerRatingPanel = 
					PanelBuilder.HORIZONTAL(
							"rating-summary-item", 
							ratingLabel, 
							this.managerRatingField);
			this.managerRatingPanel.setExpandRatio(ratingLabel, 3);
			this.managerRatingPanel.setExpandRatio(this.managerRatingField, 1);
			this.managerRatingPanel.setSpacing(true);
			
			ratingLabel = LabelBuilder.build("Review rating: ", Style.Text.BOLDED);
			this.reviewRatingPanel = 
					PanelBuilder.HORIZONTAL(
							"rating-summary-item", 
							ratingLabel, 
							this.reviewRatingField);
			this.reviewRatingPanel.setExpandRatio(ratingLabel, 3);
			this.reviewRatingPanel.setExpandRatio(this.reviewRatingField, 1);
			this.reviewRatingPanel.setSpacing(true);
			
			switch (rating.getAssessment().getStatus()) {
				case Created: this.ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField, employeeRatingPanel); break;
				case EmployeeCompleted: this.ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField, employeeRatingPanel, managerRatingPanel); break;
				case ManagerCompleted: 
				case Reviewed: this.ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField, employeeRatingPanel, managerRatingPanel, reviewRatingPanel); break;
				default: this.ratingPanel = PanelBuilder.VERTICAL(weightField, ratingField, scoreField);
			}
			addComponent(ratingPanel, 2, 0, 2, 2);

			setColumnExpandRatio(0, 3f);
			setColumnExpandRatio(1, 3f);
			setColumnExpandRatio(2, 1f);
			
			updateFieldAccess();
			registerDirtyListener();
		}
		
		public void setCurrentUser(final User currentUser) {
			this.currentUser = currentUser;
		}
		
		protected void updateFieldAccess() {
			
			this.areaField.setReadOnly(isAreaReadOnly());
			this.measurementField.setReadOnly(isMeasurementReadOnly());
			this.managerCommentField.setReadOnly(isManagerCommentReadOnly());
			this.employeeCommentField.setReadOnly(isEmployeeCommentReadOnly());
			this.weightField.setReadOnly(isWeightReadOnly());
			this.ratingField.setReadOnly(isRatingReadOnly());
		}

		public AssessmentRating getValue() {
			return fieldGroup.getItemDataSource().getBean();
		}
		
		public boolean isCurrentUserAdmin() {
			return this.currentUser != null && this.currentUser.isAdmin();
		}
		
		public boolean isCurrentUserManager() {
			return getValue().getAssessment().getManager().equals(currentUser);
		}
		
		public boolean isCurrentUserEmployee() {
			return getValue().getAssessment().getEmployee().equals(currentUser);
		}
		
		@SuppressWarnings("unused")
		public boolean isCreatingStatus() {
			return getValue().getAssessment().getStatus() == AssessmentStatus.Creating;
		}
		
		public boolean isCreatedStatus() {
			return getValue().getAssessment().getStatus() == AssessmentStatus.Created;
		}
		
		public boolean isEmployeeCompletedStatus() {
			return getValue().getAssessment().getStatus() == AssessmentStatus.EmployeeCompleted;
		}
		
		public boolean isManagerCompletedStatus() {
			return getValue().getAssessment().getStatus() == AssessmentStatus.ManagerCompleted;
		}
		
		public boolean isReviewedStatus() {
			return getValue().getAssessment().getStatus() == AssessmentStatus.Reviewed;
		}
		
		public boolean isAreaReadOnly() {
			return !((isCurrentUserAdmin() || isCurrentUserManager()) && !isReviewedStatus());
		}
		
		public boolean isMeasurementReadOnly() {
			return !((isCurrentUserAdmin() || isCurrentUserManager()) && !isReviewedStatus());
		}
		
		public boolean isEmployeeCommentReadOnly() {
			return !(isCurrentUserEmployee() && (isCreatedStatus()));
		}
		
		public boolean isManagerCommentReadOnly() {
			return !(isCurrentUserManager() && (isEmployeeCompletedStatus() || isManagerCompletedStatus()));
		}
		
		public boolean isWeightReadOnly() {
			return !((isCurrentUserAdmin() || isCurrentUserManager()) && !isReviewedStatus());
		}
		
		public boolean isRatingReadOnly() {
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
		
		public void setPossibleWeightings(final Collection<?> weightings) {
			this.weightField.removeAllItems();
			this.weightField.addItems(weightings);
		}
		
		public void setPossibleRatings(final Collection<?> ratings) {
			this.ratingField.removeAllItems();
			this.ratingField.addItems(ratings);
		}

		public void commit() {
			try {
				this.fieldGroup.commit();
			} catch (CommitException e) {
				throw new RuntimeException("Unable to commit assessment rating", e);
			}
		}
		
		public void addDirtyListener(final DirtyListener listener) {
			if (listener != null) {
				this.dirtyListeners.add(listener);
			}
		}
		
		@SuppressWarnings("unused")
		public void removeDirtyListener(final DirtyListener listener) {
			if (listener != null) {
				this.dirtyListeners.remove(listener);
			}
		}
		
		protected void registerDirtyListener() {
			for(Field<?> field : this.fieldGroup.getFields()) {
				field.removeValueChangeListener(this::onValueChange);
				field.addValueChangeListener(this::onValueChange);
			}
		}
		
		protected void onValueChange(final com.vaadin.data.Property.ValueChangeEvent event) {
			
			boolean ratingChanged = ratingField.equals(event.getProperty());
			boolean recalculationRequired = weightField.equals(event.getProperty()) || ratingChanged;
			
			if (recalculationRequired) {
				this.scoreField.markAsDirty();
			}
			
			if (ratingChanged) {
				onRatingChange((Double) ratingField.getValue());
			}
			
			for (DirtyListener listener : dirtyListeners) {
				listener.onDirty(new FormDirtyEvent(event.getProperty(), recalculationRequired));
			}
		}
		
		protected void onRatingChange(Double rating) {
			switch(getValue().getAssessment().getStatus()) {
			case Created:
				/* Set the employee self rating */
				getValue().setEmployeeRating(rating);
				this.readOnlyFieldGroup.getBean().setEmployeeRating(rating);
				this.employeeRatingField.markAsDirty();
				break;
			case EmployeeCompleted:
				/* Set the manager rating */
				getValue().setManagerRating(rating);
				this.readOnlyFieldGroup.getBean().setManagerRating(rating);
				this.managerRatingField.markAsDirty();
				break;
			case ManagerCompleted:
				/* Set the review rating */
				getValue().setReviewRating(rating);
				this.readOnlyFieldGroup.getBean().setReviewRating(rating);
				this.reviewRatingField.markAsDirty();
				break;
			default:
				/* Not set */
			}
		}
	}

	public class ChangeEvent implements Serializable {
		private final AssessmentRating rating;
		
		public ChangeEvent(final AssessmentRating rating) {
			this.rating = rating;
		}
		public AssessmentRating getAssessmentRating() {
			return this.rating;
		}
	}
	
	public class RecalculationEvent implements Serializable {
		private final int noOfRatings;
		private final double weightingTotal;
		private final double scoreTotal;
		
		public RecalculationEvent(int noOfRatings, double weightingTotal, double scoreTotal) {
			super();
			this.noOfRatings = noOfRatings;
			this.weightingTotal = weightingTotal;
			this.scoreTotal = scoreTotal;
		}

		public int getNoOfRatings() { return noOfRatings; }
		public double getWeightingTotal() { return weightingTotal; }
		public double getScoreTotal() { return scoreTotal; }
	}
	
	public interface AssessmentRatingListener extends Serializable {
        void onChange(ChangeEvent event);
    }
	
	public interface RecalculationListener extends Serializable {
		void onRecalc(RecalculationEvent event);
	}
}
