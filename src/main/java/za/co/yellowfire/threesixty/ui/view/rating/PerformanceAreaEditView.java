package za.co.yellowfire.threesixty.ui.view.rating;

import org.apache.commons.lang3.StringUtils;


//@SpringView(name = PerformanceAreaEditView.VIEW_NAME)
public class PerformanceAreaEditView /*extends AbstractRepositoryEntityEditView<PerformanceArea, String>*/ {
	private static final long serialVersionUID = 1L;
	
	public static final String TITLE = "Discipline";
	public static final String VIEW_NAME = "performance-area";
	public static final String EDIT_ID = VIEW_NAME + "-edit";
    public static final String TITLE_ID = VIEW_NAME + "--title";
    
    public static final String VIEW(final String id) { return VIEW_NAME + (StringUtils.isBlank(id) ? "" : "/" + id); } 

//    @Autowired
//    public PerformanceAreaEditView(final PerformanceAreaRepository repository) {
//    	super(repository, new PerformanceAreaEntityEditForm());
//    }
//
//    @Override
//	protected String getTitle() { return TITLE; }
//	@Override
//	protected String getTitleId() { return TITLE_ID; }
//    @Override
//	protected String getEditId() { return EDIT_ID; }
//
//	@Override
//	protected void onCreate(ClickEvent event) {
//		UI.getCurrent().getNavigator().navigateTo(PerformanceAreaEditView.VIEW_NAME + "/new-performance-area");
//	}
//
//	protected User getCurrentUser() {
//		return ((MainUI) UI.getCurrent()).getCurrentUser();
//	}
}

