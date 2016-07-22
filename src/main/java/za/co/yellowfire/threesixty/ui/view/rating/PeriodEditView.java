package za.co.yellowfire.threesixty.ui.view.rating;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.UI;

import za.co.yellowfire.threesixty.domain.rating.AssessmentService;
import za.co.yellowfire.threesixty.domain.rating.Period;
import za.co.yellowfire.threesixty.domain.rating.PeriodService;
import za.co.yellowfire.threesixty.domain.user.UserService;
import za.co.yellowfire.threesixty.ui.I8n;
import za.co.yellowfire.threesixty.ui.view.AbstractEntityEditView;

@SpringView(name = PeriodEditView.VIEW_NAME)
public final class PeriodEditView extends AbstractEntityEditView<Period> {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = I8n.Period.SINGULAR;
	public static final String VIEW_NAME = "period";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "-title";
    
    public static final String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); } 

    @Autowired
    public PeriodEditView(
    		final PeriodService periodService, 
    		final AssessmentService assessmentService,
    		final UserService userService) {
    	
    	super(periodService, 
    			new PeriodEntityEditForm(periodService, assessmentService),
    			userService.getCurrentUser().isAdmin()
    			);
    }

    @Override
	protected String getTitle() { return TITLE; }
	@Override
	protected String getTitleId() { return TITLE_ID; }
    @Override
	protected String getEditId() { return EDIT_ID; }
	
	@Override
	protected void onCreate(ClickEvent event) {
		UI.getCurrent().getNavigator().navigateTo(PeriodEditView.VIEW("new-period"));
	}
}

