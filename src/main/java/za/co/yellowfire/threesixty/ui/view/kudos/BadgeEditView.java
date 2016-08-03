package za.co.yellowfire.threesixty.ui.view.kudos;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.IdealRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.view.AbstractRepositoryEntityEditView;

@SpringView(name = BadgeEditView.VIEW_NAME)
public final class BadgeEditView extends AbstractRepositoryEntityEditView<Badge, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Badge";
	public static final String EDIT_ID = "badge-edit";
    public static final String TITLE_ID = "badge-title";
    public static final String VIEW_NAME = "badge";
    public static final String VIEW(final String outcome) { return VIEW_NAME + (StringUtils.isBlank(outcome) ? "" : "/" + outcome); } 
    public static final String VIEW_NEW() { return VIEW("new_" + VIEW_NAME); }
    
    @Autowired
    public BadgeEditView(
    		final BadgeRepository repository, 
    		final IdealRepository idealRepository,
			final GridFsClient client) {
    	super(repository, new BadgeEntityEditForm(repository, idealRepository, client));
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(BadgeEditView.VIEW_NEW());
	}
	
	@Override
	protected void onPostSave(ClickEvent event) {
		super.onPostSave(event);
		
		((BadgeEntityEditForm) getForm()).onPostSave(event);
	}
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
}

