package za.co.yellowfire.threesixty.ui.component.rating;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.vaadin.viritin.fields.MTextArea;
import org.vaadin.viritin.fields.MTextField;

import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Field;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import za.co.yellowfire.threesixty.domain.rating.AssessmentRating;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.field.MComboBox;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.DirtyEvent;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.DirtyListener;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.FormDirtyEvent;

@SuppressWarnings("serial")
public class AssessmentRatingsField extends CustomField<Set<AssessmentRating>> {

	private VerticalLayout layout;
	private LinkedList<AssessmentRatingPanel> panels = new LinkedList<>();
	private LinkedList<Double> possibleRatings = new LinkedList<>();
	private LinkedList<Double> possibleWeightings = new LinkedList<>();
	private AssessmentRatingListener listener;
	private RecalculationListener recalculationListener;
	
	private AssessmentStatus assessmentStatus = AssessmentStatus.Creating;
	
	public AssessmentRatingsField(
			final Collection<Double> possibleRatings, 
			final Collection<Double> possibleWeightings) {
		this(possibleRatings, possibleWeightings, null, null);
	}
	
	public AssessmentRatingsField(
			final Collection<Double> possibleRatings, 
			final Collection<Double> possibleWeightings,
			final AssessmentRatingListener listener,
			final RecalculationListener recalculationListener) {
		
		this.possibleRatings.addAll(possibleRatings);
		this.possibleWeightings.addAll(possibleWeightings);
		this.listener = listener;
		this.recalculationListener = recalculationListener;
		
		this.setReadOnly(false);
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
		
		this.layout = new VerticalLayout();
		this.layout.setSpacing(true);
		this.setWidth(100.0f, Unit.PERCENTAGE);
		Responsive.makeResponsive(this.layout);
		
		if (getValue() != null) {
			for(AssessmentRating rating : getValue()) {
				addAssessmentRatingPanel(rating);
			}
		} else {
			addAssessmentRating();
		}
		
		Panel panel = new Panel(this.layout);
		panel.addStyleName(ValoTheme.PANEL_BORDERLESS);
		panel.addStyleName("assessment-rating-panel");
		panel.setHeight(480, Unit.PIXELS);
		
		return panel;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends Set<AssessmentRating>> getType() {
		return (Class<? extends Set<AssessmentRating>>) Set.class;
	}

	@Override
	public void commit() throws SourceException, InvalidValueException {
		
		for(AssessmentRatingPanel panel : this.panels) {
			panel.commit();
		}
		
		super.commit();
	}

	public void setAssessmentStatus(final AssessmentStatus status) {
		this.assessmentStatus = status;
		
		for(AssessmentRatingPanel panel : this.panels) {
			panel.setAssessmentStatus(status);
		}
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
		
		addAssessmentRatingPanel(rating);
		
		getValue().add(rating);
		fireAssessmentRatingAdded(rating);
	}
	
	protected void addAssessmentRatingPanel(final AssessmentRating rating) {
		
		/* Add the the new assessment panel */
		AssessmentRatingPanel panel = new AssessmentRatingPanel(rating, assessmentStatus, this.possibleRatings, this.possibleWeightings);
		layout.addComponent(panel);
		panels.add(panel);
				
		if (layout.getComponentIndex(panel) % 2 == 0) {
			panel.addStyleName(Style.AssessmentRating.ROW);
		} else {
			panel.addStyleName(Style.AssessmentRating.ROW_ODD);
		}
		
		panel.addDirtyListener(this::onValueChange);
	}
	
	protected void fireAssessmentRatingAdded(final AssessmentRating rating) {
		if (this.listener != null) {
			this.listener.onChange(new ChangeEvent(rating));
		}
	}
	
	protected void fireRecalculationRequired() {
		if (this.recalculationListener != null) {
			
			int noOfRatings = 0;
			double weightingTotal = 0.0;
			double ratingTotal = 0.0;
			double grandTotal = 0.0;
			for(AssessmentRatingPanel panel : this.panels) {
				weightingTotal += panel.getWeighting();
				ratingTotal += panel.getRating();
				grandTotal += panel.getTotal();
				noOfRatings++;
			}
			
			this.recalculationListener.onRecalc(new RecalculationEvent(noOfRatings, weightingTotal, ratingTotal, grandTotal));
		}
	}
	
	protected void onValueChange(final DirtyEvent event) {
		getState().modified = true;
		fireValueChange(false);
		fireRecalculationRequired();
	}
	
	private static class AssessmentRatingPanel extends GridLayout {
		@PropertyId("id")
		private MTextField idField = new MTextField("Id").withReadOnly(true);
		@PropertyId("performanceArea")
		private MTextField areaField = new MTextField("Performance Area").withWidth(100.0f, Unit.PERCENTAGE);
		@PropertyId("measurement")
		private MTextArea measurementField = new MTextArea("Measurement").withRows(7);
		@PropertyId("managerComment")
		private MTextField managerCommentField = new MTextField("Manager Comment").withWidth(100.0f, Unit.PERCENTAGE);
		@PropertyId("employeeComment")
		private MTextField employeeCommentField = new MTextField("Employee Comment").withWidth(100.0f, Unit.PERCENTAGE);
		@PropertyId("weight")
		private MComboBox weightField = 
			new MComboBox("Weight")
				.withWidth(100.0f, Unit.PERCENTAGE);
		@PropertyId("rating")
		private MComboBox ratingField = 
			new MComboBox("Rating")
				.withWidth(100.0f, Unit.PERCENTAGE);
		@PropertyId("total")
		private MTextField totalField = 
			new MTextField("Total")
				.withWidth(100.0f, Unit.PERCENTAGE)
				.withReadOnly(true);
		
		private BeanFieldGroup<AssessmentRating> fieldGroup;
		private LinkedHashSet<DirtyListener> dirtyListeners = new LinkedHashSet<>();
		
		public AssessmentRatingPanel(
				final AssessmentRating rating, 
				final AssessmentStatus status,
				final Collection<?> possibleRatings, 
				final Collection<?> possibleWeightings) {
			super(3, 3);
			
			setWidth(100.0f, Unit.PERCENTAGE);
			setSpacing(true);
			
			this.fieldGroup = BeanBinder.bind(rating, this, true);
			this.ratingField.addItems(possibleRatings);
			this.weightField.addItems(possibleWeightings);
			
			/* Column 0 */
			addComponent(areaField, 0, 0);
			addComponent(managerCommentField, 0, 1);
			addComponent(employeeCommentField, 0, 2);
			
			/* Column 1 */
			addComponent(measurementField, 1, 0, 1, 2);
			
			/* Column 2 */
			addComponent(weightField, 2, 0);
			addComponent(ratingField, 2, 1);
			addComponent(totalField, 2, 2);
			
			setColumnExpandRatio(0, 3f);
			setColumnExpandRatio(1, 3f);
			setColumnExpandRatio(2, 1f);
			
			setAssessmentStatus(status);
			registerDirtyListener();
		}
		
		public void setAssessmentStatus(final AssessmentStatus status) {
			switch(status) {
			case Creating:
				this.areaField.setReadOnly(false);
				this.measurementField.setReadOnly(false);
				this.managerCommentField.setReadOnly(true);
				this.employeeCommentField.setReadOnly(true);
				this.weightField.setReadOnly(false);
				this.ratingField.setReadOnly(true);
				break;
			case Created:
				this.areaField.setReadOnly(true);
				this.measurementField.setReadOnly(true);
				this.managerCommentField.setReadOnly(true);
				this.employeeCommentField.setReadOnly(false);
				this.weightField.setReadOnly(true);
				this.ratingField.setReadOnly(false);
				break;
			case EmployeeCompleted:
				this.areaField.setReadOnly(true);
				this.measurementField.setReadOnly(true);
				this.managerCommentField.setReadOnly(false);
				this.employeeCommentField.setReadOnly(true);
				this.weightField.setReadOnly(false);
				this.ratingField.setReadOnly(false);
				break;
			case ManagerCompleted:
				this.areaField.setReadOnly(true);
				this.measurementField.setReadOnly(true);
				this.managerCommentField.setReadOnly(false);
				this.employeeCommentField.setReadOnly(false);
				this.weightField.setReadOnly(false);
				this.ratingField.setReadOnly(false);
				break;
			case Reviewed:
				this.areaField.setReadOnly(true);
				this.measurementField.setReadOnly(true);
				this.managerCommentField.setReadOnly(true);
				this.employeeCommentField.setReadOnly(true);
				this.weightField.setReadOnly(true);
				this.ratingField.setReadOnly(true);
				break;
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
		
		public double getWeighting() {
			return (Double) this.weightField.getValue();
		}
		
		public double getRating() {
			return (Double) this.ratingField.getValue();
		}
		
		public double getTotal() {
			return 0.0;
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
			if (this.fieldGroup.isModified()) {
				
				boolean recalculationRequired = false;
				if (weightField.getPropertyDataSource().equals(event.getProperty())) {
					recalculationRequired = true;
				} else if (ratingField.getPropertyDataSource().equals(event.getProperty())) {
					recalculationRequired = true;
				}
				
				for (DirtyListener listener : dirtyListeners) {
					listener.onDirty(new FormDirtyEvent(event.getProperty(), recalculationRequired));
				}
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
		private final double ratingTotal;
		private final double grandTotal;
		
		public RecalculationEvent(int noOfRatings, double weightingTotal, double ratingTotal, double grandTotal) {
			super();
			this.noOfRatings = noOfRatings;
			this.weightingTotal = weightingTotal;
			this.ratingTotal = ratingTotal;
			this.grandTotal = grandTotal;
		}

		public int getNoOfRatings() { return noOfRatings; }
		public double getWeightingTotal() { return weightingTotal; }
		public double getRatingTotal() { return ratingTotal; }
		public double getGrandTotal() { return grandTotal; }
	}
	
	public interface AssessmentRatingListener extends Serializable {
        void onChange(ChangeEvent event);
    }
	
	public interface RecalculationListener extends Serializable {
		void onRecalc(RecalculationEvent event);
	}
}
