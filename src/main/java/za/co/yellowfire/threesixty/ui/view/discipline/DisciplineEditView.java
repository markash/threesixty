package za.co.yellowfire.threesixty.ui.view.discipline;

import com.github.markash.ui.component.BlankSupplier;
import com.github.markash.ui.component.EntityPersistFunction;
import com.github.markash.ui.component.EntitySupplier;
import com.github.markash.ui.view.AbstractEntityEditView;
import com.vaadin.spring.annotation.SpringView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.domain.rating.Discipline;
import za.co.yellowfire.threesixty.ui.I8n;

import java.io.Serializable;


@SpringView(name = DisciplineEditView.VIEW_NAME)
public class DisciplineEditView extends AbstractEntityEditView<Discipline> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Discipline.SINGULAR;
	public static final String VIEW_NAME = "discipline";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); }


    @Autowired
    public DisciplineEditView(
            final EntitySupplier<Discipline, Serializable> disciplineSupplier,
            final BlankSupplier<Discipline> blankDisciplineSupplier,
            final EntityPersistFunction<Discipline> disciplinePersistFunction,
            final DisciplineEntityEditForm disciplineEntityEditForm) {

        super(TITLE,
                disciplineEntityEditForm,
                disciplineSupplier,
                blankDisciplineSupplier,
                disciplinePersistFunction);
    }

    @Override
    protected String successfulPersistNotification(
            final Discipline entity) {

        return entity.getName() + " successfully persisted.";
    }
}

