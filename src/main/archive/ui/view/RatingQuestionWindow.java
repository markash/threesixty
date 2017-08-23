package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.server.Responsive;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import org.vaadin.dialogs.ConfirmDialog;
import za.co.yellowfire.threesixty.domain.question.Questionaire;
import za.co.yellowfire.threesixty.domain.question.QuestionaireService;
import za.co.yellowfire.threesixty.domain.question.RatingQuestion;
import za.co.yellowfire.threesixty.domain.question.RatingQuestionConfiguration;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.RatingQuestionForm;
import za.co.yellowfire.threesixty.ui.component.button.FormButtons;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class RatingQuestionWindow extends Window {
	private static final long serialVersionUID = 1L;

	private Questionaire questionaire;
	private final RatingQuestionForm form;
	private final QuestionaireService service;
	private final Button saveButton = ButtonBuilder.SAVE(this::onSave);
	private final Button cancelButton = ButtonBuilder.CANCEL(this::onCancel);
	private final Button resetButton = ButtonBuilder.RESET(this::onReset);
	
	private final List<SaveListener> saveListeners = new ArrayList<>();
	
	public RatingQuestionWindow(
			final RatingQuestionConfiguration configuration, 
			final QuestionaireService service) {
		this("Rating question", configuration, service);
	}

	public RatingQuestionWindow(
			final String caption, 
			final RatingQuestionConfiguration configuration,
			final QuestionaireService service) {
		super(caption);

		this.service = service;
		this.form = new RatingQuestionForm(configuration);
		
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSpacing(true);
		Responsive.makeResponsive(root);
		root.addComponent(form);
		root.addComponent(new FormButtons(saveButton, resetButton, cancelButton));
		
		setContent(root);
	}
	
	public void setQuestionaire(final Questionaire questionaire) {
		this.questionaire = questionaire;
	}
	
	public Questionaire getQuestionaire() {
		return this.questionaire;
	}
	
	public void reset() {
		this.form.discard();
	}
	
	public void addSaveListener(SaveListener listener) {
		this.saveListeners.add(listener);
	}
	
	public void removeSaveListener(SaveListener listener) {
		this.saveListeners.remove(listener);
	}
	
	protected void fireSavedEntity(RatingQuestion object) {
		SaveEvent event = new SaveEvent(object);
		for (SaveListener listener : this.saveListeners) {
			listener.entitySaved(event);
		}
	}
	
	protected void onSave(ClickEvent event) {
		try {
			//Validate the field group
	        form.commit();
	        //Persist the rating question
	        RatingQuestion question = form.getItem();
	        questionaire.addQuestion(question);
	        questionaire = service.save(questionaire);
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", "Rating question " + question.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
	        fireSavedEntity(question);
	        UI.getCurrent().removeWindow(this);
		} catch (CommitException exception) {
            Notification.show("Error while updating rating question", Type.ERROR_MESSAGE);
        }
	}

	protected void onCancel(ClickEvent event) {
		if (form.isModified()) {
			ConfirmDialog.show(
					UI.getCurrent(), 
					"Confirmation", 
					"Would you like to discard you changes?",
					"Yes",
					"No",
			        dialog -> {
			                if (dialog.isConfirmed()) {
			                    //Discard the field group
			                	form.discard();
			                	UI.getCurrent().removeWindow(RatingQuestionWindow.this);
			                }
			            });
		} else {
			UI.getCurrent().removeWindow(this);
		}
	}
	
	protected void onReset(ClickEvent event) {
		if (form.isModified()) {
			ConfirmDialog.show(
					UI.getCurrent(), 
					"Confirmation", 
					"Would you like to discard you changes?",
					"Yes",
					"No",
			        dialog -> {
			                if (dialog.isConfirmed()) {
			                	form.discard();
			                }
			            });
		}
	}
	
	public static class SaveEvent {
		private RatingQuestion question;
		
        public SaveEvent(RatingQuestion question) {
            this.question = question;
        }

        public RatingQuestion getObject() {
            return question;
        }
    }
	
	public interface SaveListener extends Serializable {
        public void entitySaved(SaveEvent e);
    }
}
