package za.co.yellowfire.threesixty.ui.view.kudos;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = BadgeSearchView.VIEW_NAME)
public final class BadgeSearchView extends AbstractTableSearchView<Badge, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Badges";
	public static final String EDIT_ID = "badge-edit";
    public static final String TITLE_ID = "badge-title";
    public static final String VIEW_NAME = "badges";
    public static final String[] TABLE_PROPERTIES = {"id", "ideal", "description"};
    public static final String[] TABLE_FILTERS = {"id", "ideal", "description"};
    public static final String[] TABLE_HEADERS = {"Name", "Business Ideal", "Description"};
    
    protected Button newButton = ButtonBuilder.NEW(this::onCreate);
    protected Button[] tableButtons = {newButton};
    
    @Autowired
    public BadgeSearchView(BadgeRepository repository) {
    	super(Badge.class, new SpringEntityProvider<Badge>(repository), TABLE_FILTERS);
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
		UI.getCurrent().getNavigator().navigateTo(BadgeEditView.VIEW_NEW());
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(BadgeEditView.VIEW(value));
	}
	
	@Override
	protected Badge buildEmpty() {
		return null;
	}
}

