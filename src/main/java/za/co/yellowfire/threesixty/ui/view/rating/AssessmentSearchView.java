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
import org.vaadin.addons.excelexporter.ExportToExcel;
import org.vaadin.addons.excelexporter.configuration.ExportExcelComponentConfiguration;
import org.vaadin.addons.excelexporter.configuration.ExportExcelConfiguration;
import org.vaadin.addons.excelexporter.configuration.ExportExcelSheetConfiguration;
import org.vaadin.addons.excelexporter.configuration.builder.ComponentHeaderConfigurationBuilder;
import org.vaadin.addons.excelexporter.configuration.builder.ExportExcelComponentConfigurationBuilder;
import org.vaadin.addons.excelexporter.configuration.builder.ExportExcelConfigurationBuilder;
import org.vaadin.addons.excelexporter.configuration.builder.ExportExcelSheetConfigurationBuilder;
import org.vaadin.addons.excelexporter.formatter.BooleanColumnFormatter;
import org.vaadin.addons.excelexporter.formatter.ColumnFormatter;
import org.vaadin.addons.excelexporter.formatter.SuffixColumnFormatter;
import org.vaadin.addons.excelexporter.model.ExportType;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.rating.Assessment;
import za.co.yellowfire.threesixty.ui.I8n;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
		getToolbar().addAction(new MButton(VaadinIcons.DOWNLOAD_ALT, this::onExportToXlsx));
    }

	public void onCreate(
	        final ClickEvent event) {

		UI.getCurrent().getNavigator().navigateTo(AssessmentEditView.VIEW("/new-assessment"));
	}

	public void onExportToXlsx(
			final ClickEvent event) {

		ExportToExcel<Assessment> exportToExcelUtility = customizeExportExcelUtility(ExportType.XLSX);
		exportToExcelUtility.export();
	}

	/**
	 * Configuring ExportToExcel Utility This configuration allows the end
	 * user-developer to \ Add multiple sheets and configure them separately \
	 * Configure components to be added in each sheet and their properties
	 *
	 * @param exportType
	 *
	 * @return ExportToExcelUtility
	 */
	private ExportToExcel<Assessment> customizeExportExcelUtility(ExportType exportType) {

		Map<Object, ColumnFormatter> columnFormatters = new HashMap<>();

		// Suffix Formatter provided
		columnFormatters.put("totalCosts", new SuffixColumnFormatter("$"));
		columnFormatters.put("differenceToMin", new SuffixColumnFormatter("$"));
		columnFormatters.put("cheapest", new SuffixColumnFormatter("-quite cheap"));

		// Boolean Formatter provided
		columnFormatters.put("active", new BooleanColumnFormatter("Yes", "No"));

		// Custom Formatting also possible
		columnFormatters.put("catalogue", (final Object value, final Object itemId,
										   final Object columnId) -> (value != null) ? ((String) value).toLowerCase() : null);

		ExportExcelComponentConfiguration<Assessment> componentConfig1 = new ExportExcelComponentConfigurationBuilder<Assessment>()
				.withGrid(getGrid())
				.withVisibleProperties(new String[]{Assessment.FIELD_EMPLOYEE})
				.withHeaderConfigs(Arrays.asList(new ComponentHeaderConfigurationBuilder().withAutoFilter(true)
						.withColumnKeys(new String[] {"Employee"})
						.build()))
				.withIntegerFormattingProperties(Arrays.asList("counter"))
				.withFloatFormattingProperties(Arrays.asList("totalCosts", "differenceToMin"))
				.withBooleanFormattingProperties(Arrays.asList("active"))
				.withColumnFormatters(columnFormatters)
				.build();

		/* Configuring Sheets */
		ExportExcelSheetConfiguration<Assessment> sheetConfig1 = new ExportExcelSheetConfigurationBuilder<Assessment>()
				.withReportTitle("Grid (Default)")
				.withSheetName("Grid (default)")
				.withComponentConfigs(Arrays.asList(componentConfig1))
				.withIsHeaderSectionRequired(Boolean.TRUE)
				.withDateFormat("dd-MMM-yyyy")
				.build();

		/* Configuring Excel */
		ExportExcelConfiguration<Assessment> config1 = new ExportExcelConfigurationBuilder<Assessment>()
				.withGeneratedBy("Kartik Suba")
				.withSheetConfigs(Arrays.asList(sheetConfig1))
				.build();

		return new ExportToExcel<>(exportType, config1);
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

