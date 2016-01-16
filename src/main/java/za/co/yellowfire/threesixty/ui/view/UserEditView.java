package za.co.yellowfire.threesixty.ui.view;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.ByteArrayStreamResource;
import za.co.yellowfire.threesixty.ui.component.button.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm;
import za.co.yellowfire.threesixty.ui.component.field.PictureSelectionForm.FileEvent;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;
import za.co.yellowfire.threesixty.ui.view.RatingQuestionWindow.SaveEvent;
import za.co.yellowfire.threesixty.ui.view.RatingQuestionWindow.SaveListener;

@SuppressWarnings("serial")
@SpringView(name = UserEditView.VIEW_NAME)
public final class UserEditView extends AbstractDashboardPanel /*, DashboardEditListener*/ implements SaveListener {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "User";
	public static final String EDIT_ID = "user-edit";
    public static final String TITLE_ID = "user-title";
    public static final String VIEW_NAME = "user";
    public static final String VIEW(final String userName) { return VIEW_NAME + (StringUtils.isBlank(userName) ? "" : "/" + userName); } 
    public static final String[] TABLE_PROPERTIES = {"phrase"};
    public static final String[] TABLE_HEADERS = {"Phrase"};
    protected static final String[] FILTER_PROPERTIES = {"phrase"};
    
    private static final String WINDOW_PICTURE = "Profile picture";

    @PropertyId("id")
    private TextField idField = new TextField("User name");
    @PropertyId("password")
    private PasswordField passwordField = new PasswordField("Password");
    @PropertyId("firstName")
    private TextField firstNameField = new TextField("Name");
    @PropertyId("lastName")
    private TextField lastNameField = new TextField("Last Name");
    @PropertyId("title")
    private ComboBox salutationField;
    @PropertyId("gender")
    private ComboBox genderField;
    @PropertyId("email")
    private TextField emailField = new TextField("Email");
    @PropertyId("phone")
    private TextField phoneField = new TextField("Phone");
    @PropertyId("website")
    private TextField websiteField = new TextField("Website");
    @PropertyId("location")
    private ComboBox countryField;
    @PropertyId("bio")
    private RichTextArea bioField = new RichTextArea("Bio");
    @PropertyId("role")
    private ComboBox roleField;
    @PropertyId("position")
    private ComboBox positionField;
    @PropertyId("reportsTo")
    private ComboBox reportsToField;
    
    private Image pictureField = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
    private Window pictureWindow = new Window(WINDOW_PICTURE, new PictureSelectionForm(this::onSelectedPicture));
    
    private Button pictureButton = ButtonBuilder.CHANGE(this::onChangePicture);
    private Button saveButton = ButtonBuilder.SAVE(this::save);
	private Button resetButton = ButtonBuilder.RESET(this::reset);
	private Button createButton = ButtonBuilder.NEW(this::create);	
    private Button[] buttons = new Button[] {saveButton, resetButton, createButton};
   
    private BeanFieldGroup<User> fieldGroup;
    private UserService service;
    private User currentUser;
    
    @Autowired
    public UserEditView(final UserService service) {
    	super();
    	
    	this.service = service;
    	this.currentUser = service.getCurrentUser();
    	
    	this.salutationField = new ComboBox("Salutation", new IndexedContainer(service.findSalutations()));
    	this.genderField = new ComboBox("Gender", new IndexedContainer(service.findGenders()));
    	this.countryField = new ComboBox("Country", new IndexedContainer(service.findCountries()));
    	this.roleField = new ComboBox("Role", new IndexedContainer(service.findRoles()));
    	this.reportsToField = new ComboBox("Reports To");
    	this.positionField = new ComboBox("Position", new IndexedContainer(service.findPositions()));
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }

    @Override
	protected Component buildContent() {
		
    	VerticalLayout root = new VerticalLayout();
    	root.setSpacing(true);
        root.setMargin(false);
        Responsive.makeResponsive(root);
        
        root.addComponent(buildForm()); 
		return root;
	}
    
    protected Component getHeaderButtons() {
        return new HeaderButtons(buttons);
 	}
    
	protected Layout buildForm() {
		HorizontalLayout details = new HorizontalLayout();
		details.setSpacing(true);
		details.setMargin(false);
		details.setSizeFull();
		
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        idField.setNullRepresentation("");
        
        passwordField.setWidth(100.0f, Unit.PERCENTAGE);
        passwordField.setNullRepresentation("");
        
        firstNameField.setNullRepresentation("");
        firstNameField.setWidth(100.0f, Unit.PERCENTAGE);
        
        lastNameField.setNullRepresentation("");
        lastNameField.setWidth(100.0f, Unit.PERCENTAGE);
        
        salutationField.setWidth(100.0f, Unit.PERCENTAGE);
        genderField.setWidth(100.0f, Unit.PERCENTAGE);
        pictureField.setWidth(100.0f, Unit.PIXELS);
        pictureButton.setWidth(100.0f, Unit.PIXELS);
        
        emailField.setNullRepresentation("");
        emailField.setWidth(100.0f, Unit.PERCENTAGE);
        
        phoneField.setNullRepresentation("");
        phoneField.setWidth(100.0f, Unit.PERCENTAGE);
        
        websiteField.setNullRepresentation("");
        websiteField.setWidth(100.0f, Unit.PERCENTAGE);
        
        countryField.setWidth(100.0f, Unit.PERCENTAGE);
        
        bioField.setWidth(100.0f, Unit.PERCENTAGE);
        bioField.setNullRepresentation("");
        
        roleField.setWidth(100.0f, Unit.PERCENTAGE);
        positionField.setWidth(100.0f, Unit.PERCENTAGE);
        reportsToField.setWidth(100.0f, Unit.PERCENTAGE);
        
        details.addComponent(buildPanel(
        		buildHorizontalPanel(
        				buildVerticalPanel(pictureField, pictureButton), 
        				buildVerticalPanel(idField, passwordField)), 
        		firstNameField, 
        		lastNameField, 
        		buildHorizontalPanel(salutationField, genderField),
        		positionField,
        		buildHorizontalPanel(roleField, reportsToField)
        		));
        details.addComponent(buildPanel(
        		emailField, 
        		phoneField, 
        		websiteField, 
        		countryField, 
        		bioField));
        
        return details;
	}
	
	@Override
    public void enter(final ViewChangeEvent event) {
		
		reportsToField.setContainerDataSource(new IndexedContainer(service.findUsersExcept(getUser())));
		
		String[] parameters = event.getParameters().split("/");
		if (parameters.length > 0) {
			try {
				User object = getService().findUser(parameters[0]);
				this.fieldGroup = BeanBinder.bind(object != null ? object : User.EMPTY(), this, true);
			} catch (IOException e) {
				NotificationBuilder.showNotification("Loading user error", e.getMessage(), 10);
			}
		}
			
		build();
		updateFieldContraints();
		updatePicture();
    }
	
	@Override
	public void entitySaved(SaveEvent e) {
	}
	
	protected void save(ClickEvent event) {
		try {
			//Validate the field group
	        getFieldGroup().commit();
	        //Persist the user
	        User user = getService().save(getFieldGroup().getItemDataSource().getBean(), currentUser);
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", "User " + user.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
		} catch (IOException exception) {
            Notification.show("Error while updating user profile picture", exception.getMessage(), Type.ERROR_MESSAGE);
		} catch (CommitException exception) {
            Notification.show("Error while updating user", Type.ERROR_MESSAGE);
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
			                    //Set a new data source
			                    getFieldGroup().setItemDataSource(User.EMPTY());
			                }
			            }
			        });
		} else {
			getFieldGroup().discard();
			getFieldGroup().setItemDataSource(User.EMPTY());
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
			                	//Delete the user
			                    getService().delete(getFieldGroup().getItemDataSource().getBean(), currentUser);
			                    //Notify the user of the outcome
			                    NotificationBuilder.showNotification("Update", "Rating question updated successfully", 2000);
			                    //Discard the field group
			                    getFieldGroup().discard();
			                    //DashboardEventBus.post(new ProfileUpdatedEvent());
			                }
			            }
			        });
		} catch (Exception e) {
            Notification.show("Error while deleting user", Type.ERROR_MESSAGE);
        }
	}
	
	protected void reset(ClickEvent event) {
		getFieldGroup().discard();
	}
	
	protected void create(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(UserEditView.VIEW_NAME + "/new-user");
	}
	
	protected void onChangePicture(ClickEvent event) {
		UI.getCurrent().addWindow(pictureWindow);
	}
	
	protected void onSelectedPicture(FileEvent event) {
		try {
			if (getUser() != null) {
				getUser().setPicture(event.getFile());
				updatePicture();
			}
		} catch (IOException e) {
			Notification.show("Error changing profile picture", e.getMessage(), Type.ERROR_MESSAGE);
		}
	}
	
	protected void updateFieldContraints() {
		idField.setEnabled(getUser().isNew());
		
		if (currentUser != null && currentUser.isAdmin()) {
			roleField.setEnabled(true);
			positionField.setEnabled(true);
			reportsToField.setEnabled(true);
		} else {
			roleField.setEnabled(false);
			positionField.setEnabled(false);
			reportsToField.setEnabled(false);
		}
	}
	
	protected void updatePicture() {
		
		if (getUser().hasPicture()) {
			this.pictureField.setSource(new ByteArrayStreamResource(getUser().getPictureContent(), getUser().getPictureName()));
			this.pictureField.setImmediate(true);
		}
	}
	
	protected User getUser() {
		return this.fieldGroup != null ? this.fieldGroup.getItemDataSource().getBean() : User.EMPTY();
	}
	
	protected String[] getTablePropertyNames() { return TABLE_PROPERTIES; }
	protected String[] getTablePropertyHeaders() { return TABLE_HEADERS; }
	protected BeanFieldGroup<User> getFieldGroup() { return this.fieldGroup; }
	protected UserService getService() { return this.service; }
}

