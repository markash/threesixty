package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.rating.Outcome;
import za.co.yellowfire.threesixty.domain.rating.OutcomeRepository;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = OutcomesSearchView.VIEW_NAME)
public final class OutcomesSearchView extends AbstractTableSearchView<Outcome, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Outcomes";
	public static final String EDIT_ID = "outcome-edit";
    public static final String TITLE_ID = "outcome-title";
    public static final String VIEW_NAME = "outcomes";
    public static final String[] TABLE_PROPERTIES = {"id"};
    public static final String[] TABLE_FILTERS = {"id"};
    public static final String[] TABLE_HEADERS = {"Name"};
    
    protected Button newButton = ButtonBuilder.NEW(this::onCreate);
    protected Button[] tableButtons = {newButton};
    
    @Autowired
    public OutcomesSearchView(OutcomeRepository repository) {
    	super(Outcome.class, new SpringEntityProvider<Outcome>(repository), TABLE_FILTERS);
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
		UI.getCurrent().getNavigator().navigateTo(OutcomeEditView.VIEW("/new-outcome"));
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(OutcomeEditView.VIEW(value));
	}
	
	@Override
	protected Outcome buildEmpty() {
		return null;
	}
}

