package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import io.threesixty.ui.view.AbstractTableSearchView;
import io.threesixty.ui.view.TableDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.ui.I8n;

@Secured({"ROLE_ADMIN", "ROLE_USER"})
@SideBarItem(sectionId = Sections.DASHBOARD, caption = AssessmentEditView.TITLE)
@VaadinFontIcon(VaadinIcons.BOOKMARK_O)
@SpringView(name = AssessmentSearchView.VIEW_NAME)
public class AssessmentSearchView extends AbstractTableSearchView<Assessment, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Assessment.PLURAL;
	public static final String VIEW_NAME = "assessments";

//    public static final String[] TABLE_FILTERS = {
//    		Assessment.FIELD_ID,
//    		Assessment.FIELD_EMPLOYEE,
//    		Assessment.FIELD_PERIOD,
//    		Assessment.FIELD_SCORE,
//    		Assessment.FIELD_STATUS};

//    private PeriodFilter periodFilter;
    
    @Autowired
    public AssessmentSearchView(
    		final ListDataProvider<Assessment> assessmentListDataProvider,
			final TableDefinition<Assessment> assessmentTableDefinition) {
    	super(Assessment.class, TITLE, assessmentListDataProvider, assessmentTableDefinition);

//    	addHeaderComponent(new ComboBox(null, new IndexedContainer(service.findActivePeriods())).valueChangeListener(this::onPeriodFilter));

		getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

	public void onCreate(
	        final ClickEvent event) {

		UI.getCurrent().getNavigator().navigateTo(AssessmentEditView.VIEW("/new-assessment"));
	}

//	protected void onPeriodFilter(final HasValue.ValueChangeEvent event) {
//		getPeriodFilter().setPeriod((Period) event.getProperty().getValue());
//
//		Filterable data = (Filterable) getTable().getContainerDataSource();
//		data.removeContainerFilter(getPeriodFilter());
//		data.addContainerFilter(getPeriodFilter());
//	}
//
//	protected PeriodFilter getPeriodFilter() {
//
//		if (this.periodFilter == null) {
//			this.periodFilter = new PeriodFilter();
//		}
//
//		return this.periodFilter;
//	}

//	@SuppressWarnings("serial")
//	public static class PeriodFilter implements Container.Filter {
//
//		private Period selected = null;
//
//		public PeriodFilter() {}
//
//		public void setPeriod(final Period selected) { this.selected = selected; }
//
//		@Override
//		public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {
//			if (selected == null) { return true; }
//			return (itemId instanceof Assessment) && (((Assessment) itemId).getPeriod().equals(selected));
//		}
//
//		@Override
//		public boolean appliesToProperty(Object propertyId) {
//			return propertyId.equals(Assessment.FIELD_PERIOD);
//		}
//	}
}

