package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.event.EventRouter;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import za.co.yellowfire.threesixty.domain.rating.AssessmentRating;
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.domain.user.User;

import java.util.*;

@SuppressWarnings("serial")
public class AssessmentRatingsField extends CustomField<Set<AssessmentRating>> {
	/* Tabs and panels */
	private TabSheet tabSheet = new TabSheet();
	private LinkedList<AssessmentRatingPanel> panels = new LinkedList<>();

	/* Fields */
	private LinkedList<Double> possibleRatings = new LinkedList<>();
	private LinkedList<Double> possibleWeightings = new LinkedList<>();
	private LinkedList<PerformanceArea> performanceAreas = new LinkedList<>();

	private User currentUser;
	private Map<AssessmentRating, Registration> ratings = new HashMap<>();

	//private AssessmentStatus assessmentStatus = AssessmentStatus.Creating;
    private EventRouter eventRouter;

	AssessmentRatingsField(
			final Collection<Double> possibleRatings, 
			final Collection<Double> possibleWeightings,
			final Collection<PerformanceArea> performanceAreas) {
		
		this.possibleRatings.addAll(possibleRatings);
		this.possibleWeightings.addAll(possibleWeightings);
		this.performanceAreas.addAll(performanceAreas);
	}

    Registration addAssessmentDirtyListener(final AssessmentDirtyListener listener) {
        return getEventRouter().addListener(AssessmentDirtyEvent.class, listener, AssessmentDirtyListener.class.getDeclaredMethods()[0]);
    }

    Registration addAssessmentRecalculationListener(final AssessmentRecalculationListener listener) {
        return getEventRouter().addListener(AssessmentRecalculationEvent.class, listener, AssessmentRecalculationListener.class.getDeclaredMethods()[0]);
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
		this.tabSheet.setWidth(100.0f, Unit.PERCENTAGE);
		Responsive.makeResponsive(this.tabSheet);

        return tabSheet;
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
    public Set<AssessmentRating> getValue() {
        return this.ratings.keySet();
    }

    @Override
    protected void doSetValue(final Set<AssessmentRating> newRatings) {
        /* Unregister the previous event registrations */
        this.ratings.forEach((key, registration) -> registration.remove());
        this.ratings.clear();

        /* Remove rating tabs */
        for (AssessmentRatingPanel panel : this.panels) {
            this.tabSheet.removeTab(this.tabSheet.getTab(panel));
        }
        this.panels.clear();

        /* Register the ratings with event registrations */
        newRatings.forEach(rating -> this.ratings.put(rating, addAssessmentRatingPanel(rating, currentUser)));

        maintainButtons();
    }

    void setCurrentUser(final User currentUser) {
        this.currentUser = currentUser;
        maintainButtons();
    }

	private void maintainButtons() {
		for(AssessmentRatingPanel panel : this.panels) {
            panel.setCurrentUser(currentUser);
			panel.updateFieldAccess();
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

	private Registration addAssessmentRatingPanel(final AssessmentRating rating, final User currentUser) {
		
		/* Add the the new assessment panel */
		AssessmentRatingPanel panel = new AssessmentRatingPanel(rating, currentUser, this.possibleRatings, this.possibleWeightings, this.performanceAreas);
		
		Tab tab = this.tabSheet.addTab(panel);
		tab.setCaption("Rating #" + (tabSheet.getTabPosition(tab) + 1));
		panels.add(panel);
						
		return panel.addAssessmentRecalculationListener(this::onAssessmentRatingRecalculation);
	}

	private void onAssessmentRatingRecalculation(AssessmentRecalculationEvent event) {
        AssessmentSummaryModel model = new AssessmentSummaryModel();
	    this.panels.forEach(p -> model.addRating(p.getWeighting(), p.getRating()));
        getEventRouter().fireEvent(new AssessmentRecalculationEvent(this, model.getNoOfRatings(), model.getWeightingTotal(), model.getScoreTotal()));
    }
}
