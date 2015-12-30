package za.co.yellowfire.threesixty.ui.view.rating;

import java.io.Serializable;
import java.util.Collection;

import za.co.yellowfire.threesixty.domain.rating.AssessmentRating;
import za.co.yellowfire.threesixty.ui.component.rating.AssessmentRatingsField.RecalculationEvent;

@SuppressWarnings("serial")
public class AssessmentRatingSummary implements Serializable {
	private int noOfRatings;
	private double weightingTotal;
	private double ratingTotal;
	
	public int getNoOfRatings() { return noOfRatings; }
	public void setNoOfRatings(int noOfRatings) { this.noOfRatings = noOfRatings; }
	
	public double getWeightingTotal() { return weightingTotal; }
	public void setWeightingTotal(double weightingTotal) { this.weightingTotal = weightingTotal; }
	
	public double getRatingTotal() { return ratingTotal; }
	public void setRatingTotal(double ratingTotal) { this.ratingTotal = ratingTotal; }
	
	public void addRating(double rating) {
		this.ratingTotal += rating;
	}
	
	public void addWeighting(double weighting) {
		this.weightingTotal += weighting;
	}
	
	public void addAssessmentRating(final AssessmentRating rating) {
		if (rating != null) {
			addRating(rating.getRating());
			addWeighting(rating.getWeight());
		}
	}
	
	public void updateUsing(final Collection<AssessmentRating> ratings) {
		if (ratings != null) {
			setWeightingTotal(0.0);
			setRatingTotal(0.0);
			setNoOfRatings(ratings.size());
			
			for (AssessmentRating rating: ratings) {
				addAssessmentRating(rating);
			}
		}
	}
	
	public void updateUsing(final RecalculationEvent event) {
		if (event != null) {
			setWeightingTotal(event.getWeightingTotal());
			setRatingTotal(event.getRatingTotal());
			setNoOfRatings(event.getNoOfRatings());
		}
	}
}