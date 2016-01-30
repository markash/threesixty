package za.co.yellowfire.threesixty.ui.view.rating;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = PeriodSearchView.VIEW_NAME)
public final class PeriodSearchView extends AbstractTableSearchView<Period, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Period.PLURAL;
	public static final String VIEW_NAME = "periods";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String[] TABLE_PROPERTIES = {
    		Period.FIELD_ID, 
    		Period.FIELD_START, 
    		Period.FIELD_END,
    		Period.FIELD_ACTIVE};
    public static final String[] TABLE_FILTERS = {
    		Period.FIELD_ID, 
    		Period.FIELD_START, 
    		Period.FIELD_END, 
    		Period.FIELD_ACTIVE};
    public static final String[] TABLE_HEADERS = {
    		I8n.Period.Columns.ID, 
    		I8n.Period.Columns.START,
    		I8n.Period.Columns.END,
    		I8n.Period.Columns.ACTIVE};
    
    @Autowired
    public PeriodSearchView(final PeriodService service) {
    	super(Period.class, 
    			new PeriodEntityProvider(service.getPeriodRepository()), 
    			TABLE_FILTERS); 
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }

	protected String[] getTablePropertyNames() { return TABLE_PROPERTIES; }
	protected String[] getTablePropertyHeaders() { return TABLE_HEADERS; }
	
	@Override
	protected Component[] getTableButtons() {
		return new Component[] {ButtonBuilder.NEW(this::onCreate)};
	}

	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(PeriodEditView.VIEW("/new-period"));
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(PeriodEditView.VIEW(value));
	}
	
	@Override
	protected Period buildEmpty() {
		return null;
	}
}

