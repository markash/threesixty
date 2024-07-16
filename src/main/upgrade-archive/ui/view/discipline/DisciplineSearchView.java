package za.co.yellowfire.threesixty.ui.view.discipline;

import com.github.markash.ui.view.AbstractTableSearchView;
import com.github.markash.ui.view.TableDefinition;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.flow.router.Route;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.SideBarItem;
import org.vaadin.spring.sidebar.annotation.VaadinFontIcon;
import org.vaadin.viritin.button.MButton;
import za.co.yellowfire.threesixty.Sections;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.domain.rating.DisciplineRepository;
import za.co.yellowfire.threesixty.ui.I8n;

import static com.github.markash.ui.view.ValueBuilder.bool;
import static com.github.markash.ui.view.ValueBuilder.string;

@Secured({"ROLE_ADMIN"})
@SideBarItem(sectionId = Sections.DASHBOARD, caption = DisciplineEditView.TITLE, order = 3)
@VaadinFontIcon(VaadinIcons.CHECK_CIRCLE_O)
@Route(value = DisciplineSearchView.VIEW_NAME)
public class DisciplineSearchView extends AbstractTableSearchView<Discipline, String> {

    public static final String TITLE = I8n.Discipline.PLURAL;
    public static final String VIEW_NAME = "disciplines";

    @Autowired
    public DisciplineSearchView(
            final DisciplineRepository disciplineRepository) {

        super(
                Discipline.class,
                TITLE,
                new ListDataProvider<>(disciplineRepository.findAll()),
                TABLE_DEFINITION());

        getToolbar().addAction(new MButton(I8n.Button.NEW, this::onCreate));
    }

    @SuppressWarnings("unused")
    public void onCreate(Button.ClickEvent event) { UI.getCurrent().getNavigator().navigateTo(DisciplineEditView.VIEW("/new-entity")); }

    private static TableDefinition<Discipline> TABLE_DEFINITION() {

        TableDefinition<Discipline> tableDefinition =
                new TableDefinition<>(Discipline.class, DisciplineEditView.VIEW_NAME);

        tableDefinition
                .column(true)
                .withHeading(I8n.Discipline.Columns.NAME)
                .withValue(string(Discipline.FIELD_ID))
                .withDisplay(string(Discipline.FIELD_NAME))
                .enableTextSearch()
        ;

        tableDefinition
                .column()
                .withHeading(I8n.Discipline.Columns.ACTIVE)
                .withValue(bool(Discipline.FIELD_ACTIVE))
        ;

        return tableDefinition;
    }
}

