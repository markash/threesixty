package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.event.EventRouter;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Responsive;
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
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.Style;

import java.util.Collection;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class AssessmentRatingsField extends CustomField<Assessment> {
	/* Tabs and panels */
	private TabSheet tabSheet;
	private LinkedList<AssessmentRatingPanel> panels = new LinkedList<>();
	
	/* Header */
	private AssessmentSummaryHeader summary;
	private MButton addButton = new MButton("Add").withIcon(VaadinIcons.PLUS_CIRCLE).withListener(this::onAdd);
	
	/* Fields */
	private LinkedList<Double> possibleRatings = new LinkedList<>();
	private LinkedList<Double> possibleWeightings = new LinkedList<>();
	private LinkedList<PerformanceArea> performanceAreas = new LinkedList<>();

	private User currentUser;
	private Assessment assessment;
	//private AssessmentStatus assessmentStatus = AssessmentStatus.Creating;
    private EventRouter eventRouter;

	AssessmentRatingsField(
			final Collection<Double> possibleRatings, 
			final Collection<Double> possibleWeightings,
			final Collection<PerformanceArea> performanceAreas,
			final Button[] buttons) {
		
		this.possibleRatings.addAll(possibleRatings);
		this.possibleWeightings.addAll(possibleWeightings);
		this.performanceAreas.addAll(performanceAreas);
		this.summary = new AssessmentSummaryHeader(ArrayUtils.addAll(new Button[] {addButton}, buttons));
	}
	
//	public Registration addAssessmentRatingListener(final AssessmentRatingListener listener) {
//        return getEventRouter().addListener(AssessmentRatingEvent.class, listener, AssessmentRatingListener.class.getMethods()[0]);
//	}
//
//	public Registration addRecalculationListener(final AssessmentRecalculationListener listener) {
//		return getEventRouter().addListener(AssessmentRecalculationEvent.class, listener, AssessmentRecalculationListener.class.getMethods()[0]);
//	}

	@Override
	protected Component initContent() {

        this.setWidth(100.0f, Unit.PERCENTAGE);

		this.tabSheet = new TabSheet();
		Responsive.makeResponsive(this.tabSheet);
		
		if (getValue() != null) {
            this.summary.setAssessment(getValue());
		    getValue().getAssessmentRatings().forEach(rating -> addAssessmentRatingPanel(rating, currentUser));
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

    @Override
    public Assessment getValue() {
        return this.assessment;
    }

    @Override
    protected void doSetValue(final Assessment value) {
        /* Maintain the tab sheet and panels */
        this.assessment = value;

        refresh();
    }

    void refresh() {
        maintainSummary();
        maintainTabSheet();
        maintainButtons();
    }

    void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
        maintainButtons();
    }

    private boolean isAddButtonEnabled() {
		return this.assessment != null && this.assessment.getStatus().isEditingAllowed() && this.assessment.hasParticipants();
	}

	private void maintainSummary() {
        this.summary.setAssessment(getValue());
    }

	private void maintainButtons() {
		this.addButton.setEnabled(isAddButtonEnabled());
		
		for(AssessmentRatingPanel panel : this.panels) {
            panel.setCurrentUser(currentUser);
			panel.updateFieldAccess();
		}
	}

	private void maintainTabSheet() {
        if (this.tabSheet != null) {
            for (AssessmentRatingPanel panel : this.panels) {
                this.tabSheet.removeTab(this.tabSheet.getTab(panel));
            }
            panels.clear();

            if (getValue() != null) {
                getValue().getAssessmentRatings().forEach(rating -> addAssessmentRatingPanel(rating, currentUser));
            }
        }
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
		
		getValue().addAssessmentRating(rating);
		
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
