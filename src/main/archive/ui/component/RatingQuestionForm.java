package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import za.co.yellowfire.threesixty.domain.question.RatingQuestion;
import za.co.yellowfire.threesixty.domain.question.RatingQuestionConfiguration;
import za.co.yellowfire.threesixty.ui.Style;

import javax.validation.constraints.NotNull;

public class RatingQuestionForm extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	
	private static final Integer[] VALUES = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 10};
    private static final Integer[] INCREMENT = new Integer[] {1, 2};
    
	@PropertyId("id")
    private TextField idField;
    @NotNull
    @PropertyId("phrase")
    private TextField phraseField;
    @PropertyId("lowerBound")
    private ComboBox minimumField;
    @PropertyId("upperBound")
    private ComboBox maximumField;
    @PropertyId("increment")
    private ComboBox incrementField;
    
    private BeanFieldGroup<RatingQuestion> fieldGroup;
    private final RatingQuestionConfiguration configuration;
    
    public RatingQuestionForm(final RatingQuestionConfiguration configuration) {
    	this.configuration = configuration;
    	
    	build();
    }
    
    public RatingQuestion getItem() {
    	return this.fieldGroup.getItemDataSource().getBean();
    }
    
    public void setItem(final RatingQuestion ratingQuestion) {
    	this.fieldGroup.setItemDataSource(ratingQuestion);
    }
    
    public void discard() {
    	this.fieldGroup.discard();
    	this.fieldGroup.setItemDataSource(RatingQuestion.EMPTY(configuration));
    }
    
    public void commit() throws CommitException {
    	this.fieldGroup.commit();
    }
    
    public boolean isModified() {
    	return this.fieldGroup.isModified();
    }
    
    protected RatingQuestionConfiguration getConfiguration() {
    	return this.configuration;
    }
    
    protected void build() {
        addStyleName(Style.Rating.FIELDS);
                
        idField = new TextField("Id");
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        idField.setEnabled(false);
        addComponent(idField);
        
        phraseField = new TextField("Phrase");
        phraseField.setWidth(100.0f, Unit.PERCENTAGE);
        addComponent(phraseField);

        minimumField = new ComboBox("Minimum");
        minimumField.setInputPrompt("Please specify");
        for (Integer value : VALUES) {
        	minimumField.addItem(value);
		}
        minimumField.setNewItemsAllowed(true);
        minimumField.setEnabled(!isRatingBoundsLocked());
        addComponent(minimumField);
        
        maximumField = new ComboBox("Maximum");
        maximumField.setInputPrompt("Please specify");
        for (Integer value : VALUES) {
        	maximumField.addItem(value);
		}
        maximumField.setNewItemsAllowed(true);
        maximumField.setEnabled(!isRatingBoundsLocked());
        addComponent(maximumField);
        
        incrementField = new ComboBox("Increment");
        incrementField.setInputPrompt("Please specify");
        for (Integer value : INCREMENT) {
        	incrementField.addItem(value);
		}
        incrementField.setNewItemsAllowed(true);
        incrementField.setEnabled(!isRatingBoundsLocked());
        addComponent(incrementField);
        
        this.fieldGroup = BeanBinder.bind(RatingQuestion.EMPTY(configuration), this, true);
	}
    
    private boolean isRatingBoundsLocked() {
		return getConfiguration() != null && getConfiguration().isLocked();
	}
}
