package za.co.yellowfire.threesixty.ui.view;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.dialogs.ConfirmDialog;
import org.vaadin.viritin.SortableLazyList;
import org.vaadin.viritin.fields.FilterableTable;
import org.vaadin.viritin.fields.MTable;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import za.co.yellowfire.threesixty.domain.question.Question;
import za.co.yellowfire.threesixty.domain.question.Questionaire;
import za.co.yellowfire.threesixty.domain.question.QuestionaireConfiguration;
import za.co.yellowfire.threesixty.domain.question.QuestionaireService;
import za.co.yellowfire.threesixty.domain.question.RatingQuestionConfiguration;
import za.co.yellowfire.threesixty.ui.Style;
import za.co.yellowfire.threesixty.ui.component.BeanBinder;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.button.HeaderButtons;
import za.co.yellowfire.threesixty.ui.component.field.FilterTextField;
import za.co.yellowfire.threesixty.ui.component.notification.NotificationBuilder;
import za.co.yellowfire.threesixty.ui.view.RatingQuestionWindow.SaveEvent;
import za.co.yellowfire.threesixty.ui.view.RatingQuestionWindow.SaveListener;

@SuppressWarnings("serial")
@SpringView(name = QuestionaireEditView.VIEW_NAME)
public final class QuestionaireEditView extends AbstractDashboardPanel /*, DashboardEditListener*/ implements SaveListener {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Questionaire";
	public static final String EDIT_ID = "questionaire-edit";
    public static final String TITLE_ID = "questionaire-title";
    public static final String VIEW_NAME = "questionaire";
    public static final String[] TABLE_PROPERTIES = {"phrase"};
    public static final String[] TABLE_HEADERS = {"Phrase"};
    protected static final String[] FILTER_PROPERTIES = {"phrase"};
    
    private static final String BUTTON_SAVE = "Save";
    //private static final String BUTTON_EDIT = "Edit";
    private static final String BUTTON_RESET = "Reset";
    private static final String BUTTON_NEW_QUESTION = "New...";
    private static final String BUTTON_LINK_QUESTION = "Link...";
    
    
    @PropertyId("id")
    private TextField idField = new TextField("Id");;
    @NotNull
    @PropertyId("name")
    private TextField nameField = new TextField("Name");
    @PropertyId("startDate")
    private DateField startField = new DateField("Start Date");
    @PropertyId("endDate")
    private DateField endField = new DateField("End Date");
    
    private Button saveButton = ButtonBuilder.build(BUTTON_SAVE, this::save);
	private Button resetButton = ButtonBuilder.build(BUTTON_RESET, this::reset);
	private Button linkQuestionButton = ButtonBuilder.build(BUTTON_LINK_QUESTION, FontAwesome.CHAIN, this::link);
	private Button createQuestionButton = ButtonBuilder.build(BUTTON_NEW_QUESTION, FontAwesome.ASTERISK, this::create);	
    private Button[] buttons = new Button[] {saveButton, resetButton, linkQuestionButton, createQuestionButton};
    
    private MTable<Question<?>> table;
    private BeanFieldGroup<Questionaire> fieldGroup;
    private final QuestionaireConfiguration configuration;
    private QuestionaireService service;
    private SortableLazyList.SortableEntityProvider<Question<?>> entityProvider;
    private RatingQuestionWindow ratingQuestionWindow;
    
    @Autowired
    public QuestionaireEditView(
    		final QuestionaireService service,
    		final QuestionaireConfiguration configuration,
    		final RatingQuestionConfiguration ratingConfiguration) {
    	super();
    	
    	this.service = service;
    	this.configuration = configuration;
    	this.ratingQuestionWindow = new RatingQuestionWindow("New rating question", ratingConfiguration, service);
    	this.ratingQuestionWindow.addSaveListener(this);
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
        root.addComponent(getTable());
        
		return root;
	}
    
    protected Component getHeaderButtons() {
    	return new HeaderButtons(HeaderButtons.combine(new FilterTextField<Question<?>>(getTable(), FILTER_PROPERTIES), buttons));
 	}
    
	protected Layout buildForm() {
		HorizontalLayout details = new HorizontalLayout();
		details.setSpacing(true);
		details.setMargin(false);
		details.setSizeFull();
		
        idField.setWidth(100.0f, Unit.PERCENTAGE);
        idField.setEnabled(false);
        idField.setNullRepresentation("");
        
        nameField.setWidth(100.0f, Unit.PERCENTAGE);
        
        details.addComponent(buildPanel(idField, nameField));
        details.addComponent(buildPanel(startField, endField));
        
        return details;
	}
	
	@SuppressWarnings("unchecked")
	protected MTable<Question<?>> getTable() {
		if (this.table == null) {

			this.entityProvider = new SortableLazyList.SortableEntityProvider<Question<?>>() {

				@Override
				public List<Question<?>> findEntities(int firstRow, boolean sortAscending, String property) {
					return getQuestionaire().getQuestionList();
				}

				@Override
				public int size() {
					return getQuestionaire().getNoOfQuestions();
				}
	    	};
	    	
			this.table = new FilterableTable<Question<?>>()
					.setBeans(new SortableLazyList<Question<?>>(entityProvider, entityProvider, 5))
	                .withProperties(getTablePropertyNames())
	                .withColumnHeaders(getTablePropertyHeaders());	
			
			this.table.setSizeFull();
		}
		return this.table;
	}
	
	protected Layout buildPanel(final Component...components) {
		VerticalLayout layout = new VerticalLayout();
		layout.addStyleName(Style.Rating.FIELDS);
		
		for (Component component : components) {
			layout.addComponent(component);
		}
		return layout;
	}
	
	@Override
    public void enter(final ViewChangeEvent event) {
		String[] parameters = event.getParameters().split("/");
		if (parameters.length > 0) {
			Questionaire object = getService().findQuestionaire(parameters[0]);
			this.fieldGroup = BeanBinder.bind(object != null ? object : Questionaire.EMPTY(configuration), this, true);
		}
		
		build();
    }
	
	@Override
	public void entitySaved(SaveEvent e) {
		getTable().setBeans(getQuestionaire().getQuestionList());
	}
	
	protected void save(ClickEvent event) {
		try {
			//Validate the field group
	        getFieldGroup().commit();
	        //Persist the rating question
	        Questionaire questionaire = getService().save(getFieldGroup().getItemDataSource().getBean());
	        //Notify the user of the outcome
	        NotificationBuilder.showNotification("Update", "Questionaire " + questionaire.getId() + " updated successfully.", 2000);
	        //DashboardEventBus.post(new ProfileUpdatedEvent());
	        getTable().refreshRowCache();
		} catch (CommitException exception) {
            Notification.show("Error while updating questionaire", Type.ERROR_MESSAGE);
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
			                    getFieldGroup().setItemDataSource(Questionaire.EMPTY(configuration));
			                }
			            }
			        });
		} else {
			getFieldGroup().discard();
			getFieldGroup().setItemDataSource(Questionaire.EMPTY(configuration));
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
			                    getService().delete(getFieldGroup().getItemDataSource().getBean());
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
	
	//TODO Show a popup of the questions to link
	protected void link(ClickEvent event) {
		NotificationBuilder.showNotification("Development", "Not yet implemented", 1000);
	}
	
	protected void create(ClickEvent event) {
		ratingQuestionWindow.reset();
		ratingQuestionWindow.setQuestionaire(getQuestionaire());
		ratingQuestionWindow.center();
		UI.getCurrent().addWindow(ratingQuestionWindow);
	}
	
	protected Questionaire getQuestionaire() {
		return this.fieldGroup != null ? this.fieldGroup.getItemDataSource().getBean() : Questionaire.EMPTY(configuration);
	}
	
	protected Button buildLinkQuestionButton() {
		Button button = new Button(BUTTON_LINK_QUESTION, event -> { link(event); });
		button.setIcon(FontAwesome.CHAIN);
		return button;
	}
	
	protected Button buildNewQuestionButton() {
		Button button = new Button(BUTTON_NEW_QUESTION, event -> { create(event); });
		button.setIcon(FontAwesome.ASTERISK);
		return button;
	}
	
	protected String[] getTablePropertyNames() { return TABLE_PROPERTIES; }
	protected String[] getTablePropertyHeaders() { return TABLE_HEADERS; }
	protected BeanFieldGroup<Questionaire> getFieldGroup() { return this.fieldGroup; }
	protected QuestionaireService getService() { return this.service; }

}

