package za.co.yellowfire.threesixty.ui.view;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinSession;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.question.Questionaire;
import za.co.yellowfire.threesixty.domain.question.QuestionaireRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;

import java.time.LocalDate;

@SpringView(name = QuestionaireSearchView.VIEW_NAME)
public final class QuestionaireSearchView extends AbstractTableSearchView<Questionaire, String> /*, DashboardEditListener*/ {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Questionaires";
	public static final String EDIT_ID = "questionaire-edit";
    public static final String TITLE_ID = "questionaire-title";
    public static final String VIEW_NAME = "questionaires";
    public static final String[] TABLE_PROPERTIES = {"id", "name", "period.start", "period.end"};
    public static final String[] TABLE_FILTERS = {"name"};
    public static final String[] TABLE_HEADERS = {"#", "Name", "Start", "End"};
    
    protected Button[] tableButtons;
    protected QuestionaireRepository repository;
    
    @Autowired
    public QuestionaireSearchView(QuestionaireRepository repository) {
    	super(Questionaire.class, new SpringEntityProvider<Questionaire>(repository), TABLE_FILTERS);
    	this.repository = repository;
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }

	
	
	protected String[] getTablePropertyNames() { return TABLE_PROPERTIES; }
	protected String[] getTablePropertyHeaders() { return TABLE_HEADERS; }
	
	protected Button[] getTableButtons() { 
		if (this.tableButtons == null) {
			this.tableButtons = new Button[] { buildCreateButton() };
		}
		return this.tableButtons; 
	}
	
	@Override
	protected void onCreate(ClickEvent event) {
		Questionaire questionaire = 
				(Questionaire) Questionaire.name("Test")
				.startingOn(LocalDate.of(2015, 10, 20))
				.closingOn(LocalDate.of(2015, 10, 31))
				.audit((User) VaadinSession.getCurrent().getAttribute(User.class.getName()));
		
		questionaire = repository.save(questionaire);
		
		getUI().getNavigator().navigateTo(QuestionaireEditView.VIEW_NAME + "/" + questionaire.getId());
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(QuestionaireEditView.VIEW_NAME + "/" + value);
	}
	
	private Button buildCreateButton() {
        final Button button = new Button("New");
        button.setIcon(FontAwesome.PENCIL);
        button.setDescription("Create a new questionaire");
        button.addClickListener(event -> { onCreate(event); });
        return button;
    }
	
	@Override
	protected Questionaire buildEmpty() {
		return null;
	}
}

