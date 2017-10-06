package za.co.yellowfire.threesixty.ui.view.objective;

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
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.rating.Objective;
import za.co.yellowfire.threesixty.ui.I8n;

@Secured("ROLE_ADMIN")
@SideBarItem(sectionId = Sections.DASHBOARD, caption = ObjectiveEditView.TITLE)
@VaadinFontIcon(VaadinIcons.STAR_O)
@SpringView(name = ObjectiveSearchView.VIEW_NAME)
public class ObjectiveSearchView extends AbstractTableSearchView<Objective, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Objective.PLURAL;
    public static final String VIEW_NAME = "objectives";
    public static final VaadinIcons ICON = VaadinIcons.STAR_O;

    @Autowired
    public ObjectiveSearchView(
            final ListDataProvider<Objective> objectiveListDataProvider,
            final TableDefinition<Objective> objectiveTableDefinition) {
        super(Objective.class, TITLE, objectiveListDataProvider, objectiveTableDefinition);
    }

    @Override
    public void onCreate(ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(ObjectiveEditView.VIEW("/new-outcome")); }
}

