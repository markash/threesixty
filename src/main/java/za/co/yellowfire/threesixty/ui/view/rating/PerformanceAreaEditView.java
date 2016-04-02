package za.co.yellowfire.threesixty.ui.view.rating;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.rating.PerformanceArea;
import za.co.yellowfire.threesixty.domain.rating.PerformanceAreaRepository;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.view.AbstractRepositoryEntityEditView;

@SpringView(name = PerformanceAreaEditView.VIEW_NAME)
public final class PerformanceAreaEditView extends AbstractRepositoryEntityEditView<PerformanceArea, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Discipline";
	public static final String VIEW_NAME = "performance-area";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "--title";
    
    public static final String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); } 

    @Autowired
    public PerformanceAreaEditView(final PerformanceAreaRepository repository) {
    	super(repository, new PerformanceAreaEntityEditForm());
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(PerformanceAreaEditView.VIEW_NAME + "/new-performance-area");
	}
	
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
}

