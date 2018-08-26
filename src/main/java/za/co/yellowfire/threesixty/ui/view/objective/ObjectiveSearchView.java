package za.co.yellowfire.threesixty.ui.view.objective;

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
import za.co.yellowfire.threesixty.domain.rating.Objective;
import za.co.yellowfire.threesixty.ui.I8n;

@Secured("ROLE_ADMIN")
@SideBarItem(sectionId = Sections.DASHBOARD, caption = ObjectiveEditView.TITLE, order = 2)
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

        getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

    @SuppressWarnings("unused")
    public void onCreate(ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(ObjectiveEditView.VIEW("/new-outcome")); }
}

