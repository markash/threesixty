package za.co.yellowfire.threesixty.ui.view.kudos;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.domain.kudos.Ideal;
import za.co.yellowfire.threesixty.domain.kudos.IdealRepository;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.component.SpringEntityProvider;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = IdealSearchView.VIEW_NAME)
public final class IdealSearchView extends AbstractTableSearchView<Ideal, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Ideals";
	public static final String EDIT_ID = "ideal-edit";
    public static final String TITLE_ID = "ideal-title";
    public static final String VIEW_NAME = "ideals";
    public static final String[] TABLE_PROPERTIES = {"id"};
    public static final String[] TABLE_FILTERS = {"id"};
    public static final String[] TABLE_HEADERS = {"Name"};
    
    protected Button newButton = ButtonBuilder.NEW(this::onCreate);
    protected Button[] tableButtons = {newButton};
    
    @Autowired
    public IdealSearchView(IdealRepository repository) {
    	super(Ideal.class, new SpringEntityProvider<Ideal>(repository), TABLE_FILTERS);
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
		UI.getCurrent().getNavigator().navigateTo(IdealEditView.VIEW_NEW());
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(IdealEditView.VIEW(value));
	}
	
	@Override
	protected Ideal buildEmpty() {
		return null;
	}
}

