package za.co.yellowfire.threesixty.ui.view.kudos;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.kudos.Ideal;
import za.co.yellowfire.threesixty.domain.kudos.IdealRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.view.AbstractRepositoryEntityEditView;

@SpringView(name = IdealEditView.VIEW_NAME)
public final class IdealEditView extends AbstractRepositoryEntityEditView<Ideal, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Ideal";
	public static final String EDIT_ID = "ideal-edit";
    public static final String TITLE_ID = "ideal-title";
    public static final String VIEW_NAME = "ideal";
    public static final String VIEW(final String entity) { return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity); } 
    public static final String VIEW_NEW() { return VIEW("new_" + VIEW_NAME); }
    
    @Autowired
    public IdealEditView(final IdealRepository repository, final IdealEntityEditForm form) {
    	super(repository, form);
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(IdealEditView.VIEW_NEW());
	}
	
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
}

