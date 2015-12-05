package za.co.yellowfire.threesixty.ui.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.NotificationBuilder;
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
    public static final String[] TABLE_PROPERTIES = {"phrase"};
    public static final String[] TABLE_HEADERS = {"Phrase"};
    protected static final String[] FILTER_PROPERTIES = {"phrase"};
    
    private static final String BUTTON_SAVE = "Save";
    //private static final String BUTTON_EDIT = "Edit";
    private static final String BUTTON_RESET = "Reset";
    private static final String BUTTON_NEW = "New...";
    private static final String BUTTON_CHANGE = "Change...";
    
    @PropertyId("id")
    private TextField idField = new TextField("User name");
    @PropertyId("password")
    private TextField passwordField = new TextField("Password");
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
    
    private Image pictureField = new Image(null, new ThemeResource("img/profile-pic-300px.jpg"));
    
    private Button pictureButton = ButtonBuilder.build(BUTTON_CHANGE, this::changePicture);
    private Button saveButton = ButtonBuilder.build(BUTTON_SAVE, this::save);
	private Button resetButton = ButtonBuilder.build(BUTTON_RESET, this::reset);
	private Button createButton = ButtonBuilder.build(BUTTON_NEW, FontAwesome.ASTERISK, this::create);	
    private Button[] buttons = new Button[] {saveButton, resetButton, createButton};
    

    private BeanFieldGroup<User> fieldGroup;
    private UserService service;

    @Autowired
    public UserEditView(final UserService service) {
    	super();
    	
    	this.service = service;
    	this.salutationField = new ComboBox("Salutation", new IndexedContainer(service.findSalutations()));
    	this.genderField = new ComboBox("Gender", new IndexedContainer(service.findGenders()));
    	this.countryField = new ComboBox("Country", new IndexedContainer(service.findCountries()));
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
    
    protected Component buildHeaderButtons() {
        return new HeaderButtons(buttons);
 	}
    
	protected Layout buildForm() {
		HorizontalLayout details = new HorizontalLayout();
		details.setSpacing(true);
		details.setMargin(false);
		details.setSizeFull();
		
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        idField.setEnabled(false);
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
        
        emailField.setNullRepresentation("");
        emailField.setWidth(100.0f, Unit.PERCENTAGE);
        
        phoneField.setNullRepresentation("");
        phoneField.setWidth(100.0f, Unit.PERCENTAGE);
        
        websiteField.setNullRepresentation("");
        websiteField.setWidth(100.0f, Unit.PERCENTAGE);
        
        countryField.setWidth(100.0f, Unit.PERCENTAGE);
        
        bioField.setWidth(100.0f, Unit.PERCENTAGE);
        
        details.addComponent(buildPanel(
        		buildHorizontalPanel(
        				buildVerticalPanel(pictureField, pictureButton), 
        				buildVerticalPanel(idField, passwordField)), 
        		firstNameField, 
        		lastNameField, 
        		buildHorizontalPanel(salutationField, genderField)));
        details.addComponent(buildPanel(
        		emailField, 
        		phoneField, 
        		websiteField, 
        		countryField, 
        		bioField));
        
        return details;
	}
	
	protected Layout buildPanel(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(Style.Rating.FIELDS);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	protected Layout buildVerticalPanel(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(Style.Rating.FIELDS);
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	protected Layout buildHorizontalPanel(final Component...components) {
		HorizontalLayout layout = new HorizontalLayout();
		layout.setWidth(100.0f, Unit.PERCENTAGE);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	@Override
    public void enter(final ViewChangeEvent event) {
		String[] parameters = event.getParameters().split("/");
		if (parameters.length > 0) {
			User object = getService().findUser(parameters[0]);
			this.fieldGroup = BeanBinder.bind(object != null ? object : User.EMPTY(), this, true);
		}
		
		build();
    }
	
	@Override
	public void entitySaved(SaveEvent e) {
	}
	
	protected void save(ClickEvent event) {
		try {
			//Validate the field group
	        getFieldGroup().commit();
	        //Persist the user
	        User user = getService().save(getFieldGroup().getItemDataSource().getBean());
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", "User " + user.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
	        
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
			                    getService().delete(getFieldGroup().getItemDataSource().getBean());
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
		//ratingQuestionWindow.reset();
		//ratingQuestionWindow.setQuestionaire(getQuestionaire());
		//ratingQuestionWindow.center();
		//UI.getCurrent().addWindow(ratingQuestionWindow);
	}
	
	protected void changePicture(ClickEvent event) {
		Notification.show("Not implemented in this demo");
	}
	
	protected User getUser() {
		return this.fieldGroup != null ? this.fieldGroup.getItemDataSource().getBean() : User.EMPTY();
	}
	
	protected String[] getTablePropertyNames() { return TABLE_PROPERTIES; }
	protected String[] getTablePropertyHeaders() { return TABLE_HEADERS; }
	protected BeanFieldGroup<User> getFieldGroup() { return this.fieldGroup; }
	protected UserService getService() { return this.service; }
}

