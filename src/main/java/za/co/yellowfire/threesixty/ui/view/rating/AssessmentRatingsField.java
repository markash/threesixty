package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.event.EventRouter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Registration;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.ArrayUtils;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.label.MLabel;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentRating;
import za.co.yellowfire.threesixty.domain.rating.AssessmentStatus;
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

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

	private User currentUser;
	private Assessment assessment;
	private AssessmentStatus assessmentStatus = AssessmentStatus.Creating;
	private Set<AssessmentRating> ratings;
    private EventRouter eventRouter;

	public AssessmentRatingsField(
			final Collection<Double> possibleRatings, 
			final Collection<Double> possibleWeightings,
			final Collection<PerformanceArea> performanceAreas,
			final Button[] buttons) {
		
		this.possibleRatings.addAll(possibleRatings);
		this.possibleWeightings.addAll(possibleWeightings);
		this.performanceAreas.addAll(performanceAreas);
		this.summary = new AssessmentSummaryHeader(ArrayUtils.addAll(new Button[] {addButton}, buttons));
	}
	
	public Registration addAssessmentRatingListener(final AssessmentRatingListener listener) {
        return getEventRouter().addListener(AssessmentRatingEvent.class, listener, AssessmentRatingListener.class.getMethods()[0]);
	}

	public Registration addRecalculationListener(final AssessmentRecalculationListener listener) {
		return getEventRouter().addListener(AssessmentRecalculationEvent.class, listener, AssessmentRecalculationListener.class.getMethods()[0]);
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
			//addAssessmentRating();
		}
		
		/* Create the header with the header buttons */
		Label headerCaption = new MLabel(VaadinIcons.BARCODE.getHtml() + I8n.Assessment.Rating.PLURAL)
				.withContentMode(ContentMode.HTML)
				.withStyleName(ValoTheme.LABEL_H3, ValoTheme.LABEL_NO_MARGIN);

        MHorizontalLayout header = new MHorizontalLayout(headerCaption, this.summary).withStyleName(Style.AssessmentRating.HEADER);
		header.setExpandRatio(headerCaption, 1.0f);
		header.setExpandRatio(this.summary, 2.0f);
		header.withMargin(false);

		/* Vertically layout the header and tab sheet */
        return new MVerticalLayout(header, tabSheet).withMargin(false);
	}

//	@Override
//	@SuppressWarnings("unchecked")
//	public Class<Set<AssessmentRating>> get() {
//		Set<AssessmentRating> test = new HashSet<AssessmentRating>();
//		return (Class<Set<AssessmentRating>>) test.getClass();
//	}

//	@Override
//	public void commit() throws Buffered.SourceException, InvalidValueException {
//
//		for(AssessmentRatingPanel panel : this.panels) {
//			panel.commit();
//		}
//
//		super.commit();
//	}

    /**
     * Sets the value of this field. May do sanitization or throw
     * {@code IllegalArgumentException} if the value is invalid. Typically saves
     * the value to shared state.
     *
     * @param value the new value of the field
     * @throws IllegalArgumentException if the value is invalid
     */
    @Override
    protected void doSetValue(Set<AssessmentRating> value) {
        /* Maintain the tab sheet and panels */
        this.ratings = value;

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

    /**
     * Returns the current value of this object.
     * <p>
     * <i>Implementation note:</i> the implementing class should document
     * whether null values may be returned or not.
     *
     * @return the current value
     */
    @Override
    public Set<AssessmentRating> getValue() {
        return this.ratings;
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
    	if (assessment != null) {
			this.assessment = assessment;
			this.summary.setAssessment(assessment);
			this.setValue(assessment.getAssessmentRatings());
		}
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

//	REMOVE
//	public void setPossibleWeightings(final Collection<Double> weightings) {
//		for(AssessmentRatingPanel panel : this.panels) {
//			panel.setPossibleWeightings(weightings);
//		}
//	}
//	REMOVE
//	public void setPossibleRatings(final Collection<Double> ratings) {
//		for(AssessmentRatingPanel panel : this.panels) {
//			panel.setPossibleRatings(ratings);
//		}
//	}

    private EventRouter getEventRouter() {
        if (eventRouter == null) {
            eventRouter = new EventRouter();
        }
        return eventRouter;
    }

	private void addAssessmentRating() {
		AssessmentRating rating = new AssessmentRating();
		rating.setAssessment(assessment);
		
		getValue().add(rating);
		
		AssessmentRatingPanel panel = addAssessmentRatingPanel(rating, currentUser);
		this.tabSheet.setSelectedTab(panel);
		
		fireAssessmentRatingAdded(rating);
	}
	
	private AssessmentRatingPanel addAssessmentRatingPanel(final AssessmentRating rating, final User currentUser) {
		
		/* Add the the new assessment panel */
		AssessmentRatingPanel panel = new AssessmentRatingPanel(rating, currentUser, this.possibleRatings, this.possibleWeightings, this.performanceAreas);
		
		Tab tab = this.tabSheet.addTab(panel);
		tab.setCaption("Rating #" + (tabSheet.getTabPosition(tab) + 1));
		panels.add(panel);
						
		panel.addAssessmentDirtyListener(this::onAssessmentDirty);
		
		return panel;
	}
	
	private void fireAssessmentRatingAdded(final AssessmentRating rating) {
		getEventRouter().fireEvent(new AssessmentRatingEvent(this, rating, AssessmentRatingEvent.Action.ADD));
	}

	private void onAssessmentDirty(final AssessmentDirtyEvent event) {
		if (event.isRecalculationRequired()) {
			/* Recalculate the assessment values */
            this.assessment.calculate();

            AssessmentRecalculationEvent recalculationEvent = new AssessmentRecalculationEvent(
                    this,
                    assessment.getNoOfRatings(),
                    assessment.getWeightingTotal(),
                    assessment.getScore());

            /* Fire the assessment summary */
            this.summary.recalculate(recalculationEvent);

            /* Fire the assessment recalculation values */
            getEventRouter().fireEvent(recalculationEvent);
		}
	}
	
	private void onAdd(final ClickEvent event) {
		this.addAssessmentRating();
	}
}
