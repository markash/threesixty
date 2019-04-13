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

import static com.github.markash.ui.view.ValueBuilder.*;

@Secured("ROLE_ADMIN")
@SuppressWarnings("unused")
@SideBarItem(sectionId = Sections.DASHBOARD, caption = ObjectiveEditView.TITLE, order = 2)
@VaadinFontIcon(VaadinIcons.STAR_O)
@SpringView(name = ObjectiveSearchView.VIEW_NAME)
public class ObjectiveSearchView extends AbstractTableSearchView<Objective, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Objective.PLURAL;
    public static final String VIEW_NAME = I8n.Objective.SEARCH_VIEW;

    @Autowired
    public ObjectiveSearchView(
            final ListDataProvider<Objective> objectiveListDataProvider) {

        super(Objective.class, TITLE);

        withDefinition(getTableDefinition());
        withDataProvider(objectiveListDataProvider);

        getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

    @SuppressWarnings("unused")
    public void onCreate(ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(ObjectiveEditView.VIEW("/new-entity")); }

    private TableDefinition<Objective> getTableDefinition() {

        TableDefinition<Objective> tableDefinition = new TableDefinition<>(getBeanType(), ObjectiveEditView.VIEW_NAME);
        tableDefinition
                .column()
                .withHeading(I8n.Objective.Columns.NAME)
                .withValue(string(Objective.FIELD_ID))
                .end()

                .column()
                .withHeading(I8n.Objective.Columns.TEXT)
                .withValue(string(Objective.FIELD_TEXT))
                .enableTextSearch()
                .end()

                .column()
                .withHeading(I8n.Objective.Columns.ACTIVE)
                .withValue(bool(Objective.FIELD_ACTIVE));

        return tableDefinition;
    }
}

