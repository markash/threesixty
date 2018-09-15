package za.co.yellowfire.threesixty.ui.view.discipline;

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
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.ui.I8n;

@Secured({"ROLE_ADMIN", "ROLE_USER"})
@SideBarItem(sectionId = Sections.DASHBOARD, caption = DisciplineEditView.TITLE, order = 3)
@VaadinFontIcon(VaadinIcons.CHECK_CIRCLE_O)
@SpringView(name = DisciplineSearchView.VIEW_NAME)
public class DisciplineSearchView extends AbstractTableSearchView<Discipline, String> {
	private static final long serialVersionUID = 1L;

    public static final String TITLE = I8n.Discipline.PLURAL;
    public static final String VIEW_NAME = "disciplines";

    @Autowired
    public DisciplineSearchView(
            final ListDataProvider<Discipline> disciplineListDataProvider,
            final TableDefinition<Discipline> disciplineTableDefinition) {

        super(Discipline.class, TITLE, disciplineListDataProvider, disciplineTableDefinition);

        getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

    @SuppressWarnings("unused")
    public void onCreate(Button.ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(DisciplineEditView.VIEW("/new-entity")); }
}

