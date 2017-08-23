package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.*;
import com.vaadin.ui.Notification.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;
import za.co.yellowfire.threesixty.domain.question.RatingQuestion;
import za.co.yellowfire.threesixty.domain.question.RatingQuestionConfiguration;
import za.co.yellowfire.threesixty.domain.question.RatingQuestionRepository;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;

import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@SpringView(name = RatingView.VIEW_NAME)
public final class RatingView extends AbstractTableEditorView<RatingQuestion, String> /*, DashboardEditListener*/ {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Rating Questions";
	public static final String EDIT_ID = "rating-edit";
    public static final String TITLE_ID = "rating-title";
    public static final String VIEW_NAME = "rating";
    public static final String[] TABLE_PROPERTIES = {"phrase"};
    public static final String[] TABLE_HEADERS = {"Phrase"};
    private static final Integer[] VALUES = new Integer[] {1, 2, 3, 4, 5, 6, 7, 8, 10};
    private static final Integer[] INCREMENT = new Integer[] {1, 2};
    
    private RatingQuestionConfiguration questionConfiguration;
    
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
    
    @Autowired
    public RatingView(RatingQuestionRepository questionRepository, RatingQuestionConfiguration questionConfiguration) {
    	super(RatingQuestion.class, questionRepository);
    	this.questionConfiguration = questionConfiguration;
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }

	protected Layout buildForm() {
		VerticalLayout details = new VerticalLayout();
        details.addStyleName(Style.Rating.FIELDS);
                
        idField = new TextField("Id");
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        idField.setEnabled(false);
        details.addComponent(idField);
        
        phraseField = new TextField("Phrase");
        phraseField.setWidth(100.0f, Unit.PERCENTAGE);
        details.addComponent(phraseField);

        minimumField = new ComboBox("Minimum");
        minimumField.setInputPrompt("Please specify");
        for (Integer value : VALUES) {
        	minimumField.addItem(value);
		}
        minimumField.setNewItemsAllowed(true);
        minimumField.setEnabled(!isRatingBoundsLocked());
        details.addComponent(minimumField);
        
        maximumField = new ComboBox("Maximum");
        maximumField.setInputPrompt("Please specify");
        for (Integer value : VALUES) {
        	maximumField.addItem(value);
		}
        maximumField.setNewItemsAllowed(true);
        maximumField.setEnabled(!isRatingBoundsLocked());
        details.addComponent(maximumField);
        
        incrementField = new ComboBox("Increment");
        incrementField.setInputPrompt("Please specify");
        for (Integer value : INCREMENT) {
        	incrementField.addItem(value);
		}
        incrementField.setNewItemsAllowed(true);
        incrementField.setEnabled(!isRatingBoundsLocked());
        details.addComponent(incrementField);
        
        return details;
	}
	
	protected void save(ClickEvent event) {
		try {
			//Validate the field group
	        getFieldGroup().commit();
	        //Persist the rating question
	        RatingQuestion question = getRepository().save(getFieldGroup().getItemDataSource().getBean());
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", "Rating question " + question.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
	        getTable().refreshRowCache();
		} catch (CommitException exception) {
            Notification.show("Error while updating rating question", Type.ERROR_MESSAGE);
        }
	}
	
	protected void add(ClickEvent event) {
		if (getFieldGroup().isModified()) {
			ConfirmDialog.show(
					UI.getCurrent(), 
					"Confirmation", 
					"Would you like to discard you changes?",
					"Yes",
					"No",
			        new ConfirmDialog.Listener() {
			            public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                    //Discard the field group
			                	getFieldGroup().discard();
			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
			                	getTable().refreshRowCache();
			                    //Set a new data source
			                    getFieldGroup().setItemDataSource(buildEmpty());
			                }
			            }
			        });
		} else {
			getFieldGroup().discard();
			getFieldGroup().setItemDataSource(buildEmpty());
		}
	}
	
	protected void delete(ClickEvent event) {
		try {
			ConfirmDialog.show(
					UI.getCurrent(), 
					"Confirmation", 
					"Are you sure you would like to delete this rating question?",
					"Yes",
					"No",
			        new ConfirmDialog.Listener() {
			            public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	//Delete the rating question
			                    getRepository().delete(getFieldGroup().getItemDataSource().getBean());
			                    //Notify the user of the outcome
			                    NotificationBuilder.showNotification("Update", "Rating question updated successfully", 2000);
			                    //Discard the field group
			                    getFieldGroup().discard();
			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
			                    getTable().refreshRowCache();
			                }
			            }
			        });
		} catch (Exception e) {
            Notification.show("Error while deleting rating question", Type.ERROR_MESSAGE);
        }
	}
	
	protected void reset(ClickEvent event) {
		getFieldGroup().discard();
	}
	
	protected RatingQuestion buildEmpty() {
		return RatingQuestion.EMPTY(questionConfiguration);
	}
	
	private boolean isRatingBoundsLocked() {
		return questionConfiguration != null && questionConfiguration.isLocked();
	}
	
	protected String[] getTablePropertyNames() { return TABLE_PROPERTIES; }
	protected String[] getTablePropertyHeaders() { return TABLE_HEADERS; }
}

