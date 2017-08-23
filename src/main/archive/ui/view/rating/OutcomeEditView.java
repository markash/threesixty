package za.co.yellowfire.threesixty.ui.view.rating;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import za.co.yellowfire.threesixty.MainUI;
import za.co.yellowfire.threesixty.domain.rating.Outcome;
import za.co.yellowfire.threesixty.domain.rating.OutcomeService;
import za.co.yellowfire.threesixty.domain.user.User;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditView;

@SpringView(name = OutcomeEditView.VIEW_NAME)
public final class OutcomeEditView extends AbstractEntityEditView<Outcome> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Outcome";
	public static final String EDIT_ID = "outcome-edit";
    public static final String TITLE_ID = "outcome-title";
    public static final String VIEW_NAME = "outcome";
    public static final String VIEW(final String outcome) { return VIEW_NAME + (StringUtils.isBlank(outcome) ? "" : "/" + outcome); } 

    @Autowired
    public OutcomeEditView(final OutcomeService service) {
    	super(service, new OutcomeEntityEditForm());
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(OutcomeEditView.VIEW_NAME + "/new-outcome");
	}
	
	protected User getCurrentUser() {
		return ((MainUI) UI.getCurrent()).getCurrentUser();
	}
}

