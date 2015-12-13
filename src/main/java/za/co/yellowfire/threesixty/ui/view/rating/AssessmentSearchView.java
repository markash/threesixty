package za.co.yellowfire.threesixty.ui.view.rating;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentRepository;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = AssessmentSearchView.VIEW_NAME)
public final class AssessmentSearchView extends AbstractTableSearchView<Assessment, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Assessments";
	public static final String EDIT_ID = "assessment-edit";
    public static final String TITLE_ID = "assessment-title";
    public static final String VIEW_NAME = "assessments";
    public static final String[] TABLE_PROPERTIES = {"id", "employee", "score", "status"};
    public static final String[] TABLE_FILTERS = {"id", "employee", "score", "status"};
    public static final String[] TABLE_HEADERS = {"#", "Employee", "Overall Score", "Status"};
    
    protected Button newButton = ButtonBuilder.NEW(this::onCreate);
    protected Button[] tableButtons = {newButton};
    
    @Autowired
    public AssessmentSearchView(AssessmentRepository repository) {
    	super(Assessment.class, new AssessmentEntityProvider(repository, ((MainUI) UI.getCurrent()).getCurrentUser()), TABLE_FILTERS);
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
			tableButtons = new Button[] { ButtonBuilder.NEW(this::onCreate) };
		}
		return this.tableButtons; 
	}
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(AssessmentEditView.VIEW("/new-outcome"));
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(AssessmentEditView.VIEW(value));
	}
	
	@Override
	protected Assessment buildEmpty() {
		return null;
	}
}

