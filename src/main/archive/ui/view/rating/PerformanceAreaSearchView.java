package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.domain.rating.PerformanceAreaRepository;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = PerformanceAreaSearchView.VIEW_NAME)
public final class PerformanceAreaSearchView extends AbstractTableSearchView<PerformanceArea, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Performance Areas";
	public static final String EDIT_ID = "area-edit";
    public static final String TITLE_ID = "area-title";
    public static final String VIEW_NAME = "performance-areas";
    public static final String[] TABLE_PROPERTIES = {"id"};
    public static final String[] TABLE_FILTERS = {"id"};
    public static final String[] TABLE_HEADERS = {"Name"};
    
    protected Button newButton = ButtonBuilder.NEW(this::onCreate);
    protected Button[] tableButtons = {newButton};
    
    @Autowired
    public PerformanceAreaSearchView(PerformanceAreaRepository repository) {
    	super(PerformanceArea.class, new SpringEntityProvider<PerformanceArea>(repository), TABLE_FILTERS);
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
		UI.getCurrent().getNavigator().navigateTo(PerformanceAreaEditView.VIEW("/new-performance-area"));
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(PerformanceAreaEditView.VIEW(value));
	}
	
	@Override
	protected PerformanceArea buildEmpty() {
		return null;
	}
}

