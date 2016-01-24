package za.co.yellowfire.threesixty.ui.view.kudos;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;
import za.co.yellowfire.threesixty.domain.kudos.Kudos;
import za.co.yellowfire.threesixty.domain.kudos.KudosRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.resource.BadgeClientService;
import za.co.yellowfire.threesixty.ui.component.button.CrudHeaderButtonConfig;
import za.co.yellowfire.threesixty.ui.view.AbstractRepositoryEntityEditView;

@SpringView(name = KudosEditView.VIEW_NAME)
public final class KudosEditView extends AbstractRepositoryEntityEditView<Kudos, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Kudos";
	public static final String VIEW_NAME = "kudos";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String VIEW(final String entity) { return VIEW_NAME + (StringUtils.isBlank(entity) ? "" : "/" + entity); } 
    public static final String VIEW_NEW() { return VIEW("new_" + VIEW_NAME); }
    
    @Autowired
    public KudosEditView(
    		final BadgeRepository badgeRepository, 
    		final KudosRepository repository, 
    		final UserService userService, 
    		final BadgeClientService badgeClientService,
    		final GridFsClient client) {
    	super(
    			repository, 
    			new KudosEntityEditForm(badgeRepository, repository, userService, badgeClientService, client),
    			new CrudHeaderButtonConfig().saveShowIconOnly().resetShowIconOnly().hideDelete().hideNew());
    	
    	((KudosEntityEditForm) getForm()).setHeaderButtons(getHeaderButtons());
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(KudosEditView.VIEW_NEW());
	}
	
	@Override
	protected void onPostSave(ClickEvent event) {
		super.onPostSave(event);
	}
	
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
}

