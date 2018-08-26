package za.co.yellowfire.threesixty.ui.view.period;

import com.github.markash.ui.view.AbstractTableSearchView;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.ui.I8n;

@Secured("ROLE_ADMIN")
@SideBarItem(sectionId = Sections.DASHBOARD, caption = PeriodEditView.TITLE, order = 4)
@VaadinFontIcon(VaadinIcons.CALENDAR_O)
@SpringView(name = PeriodSearchView.VIEW_NAME)
public class PeriodSearchView extends AbstractTableSearchView<PeriodModel, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Period.PLURAL;
	public static final String VIEW_NAME = "periods";
	public static final VaadinIcons ICON = VaadinIcons.CALENDAR_O;

    @Autowired
    public PeriodSearchView(
			final ListDataProvider<PeriodModel> periodListDataProvider,
			final TableDefinition<PeriodModel> periodTableDefinition) {
    	super(PeriodModel.class, TITLE, periodListDataProvider, periodTableDefinition);

		getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

    @SuppressWarnings("unused")
	public void onCreate(ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(PeriodEditView.VIEW("/new-period")); }
}

