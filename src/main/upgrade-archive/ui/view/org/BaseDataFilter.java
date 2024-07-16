package za.co.yellowfire.threesixty.ui.view.org;

public class BaseDataFilter implements DataFilter {

    private boolean showActiveOnly = false;

    @Override
    public boolean isShowActiveOnly() {
        return showActiveOnly;
    }

    @Override
    public void setShowActiveOnly(boolean showActiveOnly) {
        this.showActiveOnly = showActiveOnly;
    }
}
