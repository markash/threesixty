package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.data.Container;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.component.ButtonBuilder;
import za.co.yellowfire.threesixty.ui.view.AbstractTableSearchView;

@SpringView(name = AssessmentSearchView.VIEW_NAME)
public final class AssessmentSearchView extends AbstractTableSearchView<Assessment, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Assessment.PLURAL;
	public static final String VIEW_NAME = "assessments";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String[] TABLE_PROPERTIES = {
    		Assessment.FIELD_ID, 
    		Assessment.FIELD_EMPLOYEE, 
    		Assessment.FIELD_PERIOD, 
    		Assessment.FIELD_SCORE, 
    		Assessment.FIELD_STATUS};
    public static final String[] TABLE_FILTERS = {
    		Assessment.FIELD_ID, 
    		Assessment.FIELD_EMPLOYEE, 
    		Assessment.FIELD_PERIOD, 
    		Assessment.FIELD_SCORE, 
    		Assessment.FIELD_STATUS};
    public static final String[] TABLE_HEADERS = {
    		I8n.Assessment.Columns.ID, 
    		I8n.Assessment.Columns.EMPLOYEE,
    		I8n.Assessment.Columns.PERIOD,
    		I8n.Assessment.Columns.SCORE, 
    		I8n.Assessment.Columns.STATUS};

    private PeriodFilter periodFilter;
    
    @Autowired
    public AssessmentSearchView(final AssessmentService service) {
    	super(Assessment.class, 
    			new AssessmentEntityProvider(
    					service.getAssessmentRepository(), 
    					((MainUI) UI.getCurrent()).getCurrentUser()), 
    			TABLE_FILTERS); 
    	
    	addHeaderComponent(new ComboBox(null, new IndexedContainer(service.findActivePeriods())).valueChangeListener(this::onPeriodFilter));
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
		UI.getCurrent().getNavigator().navigateTo(AssessmentEditView.VIEW("/new-assessment"));
	}

	@Override
	protected void onTableIdClick(final ClickEvent event, final String value) {
		UI.getCurrent().getNavigator().navigateTo(AssessmentEditView.VIEW(value));
	}
	
	protected void onPeriodFilter(final ValueChangeEvent event) {
		getPeriodFilter().setPeriod((Period) event.getProperty().getValue());
		
		Filterable data = (Filterable) getTable().getContainerDataSource();
		data.removeContainerFilter(getPeriodFilter());
		data.addContainerFilter(getPeriodFilter());
	}

	protected PeriodFilter getPeriodFilter() {
	
		if (this.periodFilter == null) {
			this.periodFilter = new PeriodFilter();
		}
		
		return this.periodFilter;
	}
	
	@Override
	protected Assessment buildEmpty() {
		return null;
	}
	
	@SuppressWarnings("serial")
	public static class PeriodFilter implements Container.Filter {
		
		private Period selected = null;
		
		public PeriodFilter() {}
		
		public void setPeriod(final Period selected) { this.selected = selected; }
		
		@Override
		public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
			if (selected == null) { return true; }
			return (itemId instanceof Assessment) && (((Assessment) itemId).getPeriod().equals(selected));
		}

		@Override
		public boolean appliesToProperty(Object propertyId) {
			return propertyId.equals(Assessment.FIELD_PERIOD);
		}
	}
}

