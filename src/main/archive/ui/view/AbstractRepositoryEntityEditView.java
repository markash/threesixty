package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.v7.data.fieldgroup.FieldGroup.CommitException;
import org.springframework.data.domain.Persistable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.vaadin.dialogs.ConfirmDialog;
import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.component.button.CrudHeaderButtonConfig;
import za.co.yellowfire.threesixty.ui.component.button.CrudHeaderButtons;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditForm.DirtyEvent;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractRepositoryEntityEditView<T extends Persistable<String>, ID extends Serializable> extends AbstractDashboardPanel /*, DashboardEditListener*/ {
	private static final long serialVersionUID = 1L;
	          
	private final CrudHeaderButtons headerButtons;
    private final PagingAndSortingRepository<T, ID> repository;
    private final AbstractEntityEditForm<T> form;
    
    public AbstractRepositoryEntityEditView(
    		final PagingAndSortingRepository<T, ID> repository, 
    		final AbstractEntityEditForm<T> form) {
    	
    	this(repository, form, new CrudHeaderButtonConfig());
    }
    
    public AbstractRepositoryEntityEditView(
    		final PagingAndSortingRepository<T, ID> repository, 
    		final AbstractEntityEditForm<T> form,
    		final CrudHeaderButtonConfig buttonConfig) {
    	
		super();
		
		this.repository = repository;
		this.form = form;
		this.form.addDirtyListener(this::onDirty);
		this.headerButtons = new CrudHeaderButtons(this::onSave, this::onReset, this::onCreate, this::onDelete, buttonConfig);
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
    
    public CrudHeaderButtons getHeaderButtons() {
        return headerButtons;
 	}
       
	@SuppressWarnings("unchecked")
	@Override
    public void enter(final ViewChangeEvent event) {
		String[] parameters = event.getParameters().split("/");
		if (parameters.length > 0) {
			form.bind(findEntity((ID)parameters[0]));
		}
			
		build();
		onClean();
    }
		
	protected T findEntity(final ID id) {
		return getRepository().findOne(id);
	}
	
	protected void onSave(ClickEvent event) {
		try {
			onPreSave(event);
			//Validate the field group
	        form.commit();
	        //Persist the outcome
	        T result = getRepository().save(form.getValue());
	        onPostSave(event);
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", getTitle() + " " + result.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
	        onClean();
		} catch (CommitException exception) {
            Notification.show("Error while updating", Type.ERROR_MESSAGE);
        }
	}
	
	protected void onPreSave(final ClickEvent event) {}
	
	protected void onPostSave(final ClickEvent event) {}
	
	protected void onCreate(ClickEvent event) {
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
			                	onClean();
			                }
			            }
			        });
		} else {
			form.discard();
			form.bindToEmpty();
			onClean();
		}
	}
	
	protected void onDelete(ClickEvent event) {
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
			                    onClean();
			                }
			            }
			        });
		} catch (Exception e) {
            Notification.show("Error while deleting", Type.ERROR_MESSAGE);
        }
	}
	
	protected void onReset(final ClickEvent event) {
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
			                	form.discard();
			                	onClean();
			                }
			            }
			        });
		} else {
			form.discard();
			onClean();
		}
	}

	
	protected void onDirty(final DirtyEvent event) {
		this.headerButtons.enableSave();
		this.headerButtons.enableReset();
	}
	
	protected void onClean() {
		this.headerButtons.disableSave();
		this.headerButtons.disableReset();
	}
	
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
		
	protected AbstractEntityEditForm<T> getForm() { return this.form; }
	protected PagingAndSortingRepository<T, ID> getRepository() { return this.repository; }
}

