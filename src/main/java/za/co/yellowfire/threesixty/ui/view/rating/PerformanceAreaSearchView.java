package za.co.yellowfire.threesixty.ui.view.rating;

import com.github.markash.ui.view.AbstractTableSearchView;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.objective.ObjectiveEditView;

@Secured({"ROLE_ADMIN", "ROLE_USER"})
@SideBarItem(sectionId = Sections.DASHBOARD, caption = PerformanceAreaEditView.TITLE)
@VaadinFontIcon(VaadinIcons.CHECK_CIRCLE_O)
@SpringView(name = PerformanceAreaSearchView.VIEW_NAME)
public class PerformanceAreaSearchView extends AbstractTableSearchView<PerformanceArea, String> {
	private static final long serialVersionUID = 1L;

    public static final String TITLE = I8n.PerformanceArea.PLURAL;
    public static final String VIEW_NAME = "kpa";

    @Autowired
    public PerformanceAreaSearchView(
            final ListDataProvider<PerformanceArea> performanceAreaListDataProvider,
            final TableDefinition<PerformanceArea> performanceAreaTableDefinition) {

        super(PerformanceArea.class, TITLE, performanceAreaListDataProvider, performanceAreaTableDefinition);

        getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

    @SuppressWarnings("unused")
    public void onCreate(Button.ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(ObjectiveEditView.VIEW("/new-performance-area")); }
}

