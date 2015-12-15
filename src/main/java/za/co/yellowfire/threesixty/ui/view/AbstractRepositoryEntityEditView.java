package za.co.yellowfire.threesixty.ui.view;

import java.io.Serializable;

import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.NotificationBuilder;

@SuppressWarnings("serial")
public abstract class AbstractRepositoryEntityEditView<T extends Persistable<String>, ID extends Serializable> extends AbstractDashboardPanel /*, DashboardEditListener*/ {
	private static final long serialVersionUID = 1L;
	          
    private Button saveButton = ButtonBuilder.SAVE(this::onSave);
	private Button resetButton = ButtonBuilder.RESET(this::onReset);
	private Button createButton = ButtonBuilder.NEW(this::onCreate);	
    private Button[] buttons = new Button[] {saveButton, resetButton, createButton};
   
    private final PagingAndSortingRepository<T, ID> repository;
    private final AbstractEntityEditForm<T> form;
    
    public AbstractRepositoryEntityEditView(PagingAndSortingRepository<T, ID> repository, AbstractEntityEditForm<T> form) {
		super();
		this.repository = repository;
		this.form = form;
	}

	@Override
	protected Component buildContent() {
		
    	VerticalLayout root = new VerticalLayout();
    	root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        form.layout();
        root.addComponent(form); 
		return root;
	}
    
    protected Component buildHeaderButtons() {
        return new HeaderButtons(buttons);
 	}
    
	@Override
    public void enter(final ViewChangeEvent event) {
		String[] parameters = event.getParameters().split("/");
		if (parameters.length > 0) {
			form.bind(findEntity((ID)parameters[0]));
		}
			
		build();
    }
		
	protected T findEntity(final ID id) {
		return getRepository().findOne(id);
	}
	
	protected void onSave(ClickEvent event) {
		try {
			//Validate the field group
	        form.commit();
	        //Persist the outcome
	        T result = getRepository().save(form.getValue());
	        onPostSave(event);
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", getTitle() + " " + result.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
		} catch (CommitException exception) {
            Notification.show("Error while updating", Type.ERROR_MESSAGE);
        }
	}
	
	protected void onPostSave(final ClickEvent event) {}
	
	protected void add(ClickEvent event) {
		if (form.isModified()) {
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
			                	form.discard();
			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
			                    //Set a new data source
			                	form.bindToEmpty();
			                }
			            }
			        });
		} else {
			form.discard();
			form.bindToEmpty();
		}
	}
	
	protected void delete(ClickEvent event) {
		try {
			ConfirmDialog.show(
					UI.getCurrent(), 
					"Confirmation", 
					"Are you sure you would like to delete?",
					"Yes",
					"No",
			        new ConfirmDialog.Listener() {
			            public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	//Delete the entity
			                    getRepository().delete(form.getValue());
			                    //Notify the user of the outcome
			                    NotificationBuilder.showNotification("Update", getTitle() + " updated successfully", 2000);
			                    //Discard the field group
			                    form.discard();
			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
			                }
			            }
			        });
		} catch (Exception e) {
            Notification.show("Error while deleting", Type.ERROR_MESSAGE);
        }
	}
	
	protected void onReset(ClickEvent event) {
	}
	
	protected void onCreate(ClickEvent event) {
	}
	
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
		
	protected AbstractEntityEditForm<T> getForm() { return this.form; }
	protected PagingAndSortingRepository<T, ID> getRepository() { return this.repository; }
}

