package za.co.yellowfire.threesixty.ui.component.button;

public class CrudHeaderButtonConfig {

	private static int IDX_SAVE = 0;
	private static int IDX_RESET = 1;
	private static int IDX_NEW = 2;
	private static int IDX_DELETE = 3;
	
	private boolean[] showButtons = new boolean[] {true, true, true, true};
	private boolean[] showIconsOnly = new boolean[] {false, false, false, false};
	
	public boolean isShowSave() { return this.showButtons[IDX_SAVE]; }
	public boolean isShowReset() { return this.showButtons[IDX_RESET]; }
	public boolean isShowNew() { return this.showButtons[IDX_NEW]; }
	public boolean isShowDelete() { return this.showButtons[IDX_DELETE]; }
	
	public void setShowSave(final boolean show) { this.showButtons[IDX_SAVE] = show; }
	public void setShowReset(final boolean show) { this.showButtons[IDX_RESET] = show; }
	public void setShowNew(final boolean show) { this.showButtons[IDX_NEW] = show; }
	public void setShowDelete(final boolean show) { this.showButtons[IDX_DELETE] = show; }
	
	public void setShowSaveIconOnly(final boolean show) { this.showIconsOnly[IDX_SAVE] = show; }
	public void setShowResetIconOnly(final boolean show) { this.showIconsOnly[IDX_RESET] = show; }
	public void setShowNewIconOnly(final boolean show) { this.showIconsOnly[IDX_NEW] = show; }
	public void setShowDeleteIconOnly(final boolean show) { this.showIconsOnly[IDX_DELETE] = show; }
	
	public boolean isShowSaveIconOnly() { return this.showIconsOnly[IDX_SAVE]; }
	public boolean isShowResetIconOnly() { return this.showIconsOnly[IDX_RESET]; }
	public boolean isShowNewIconOnly() { return this.showIconsOnly[IDX_NEW]; }
	public boolean isShowDeleteIconOnly() { return this.showIconsOnly[IDX_DELETE]; }
	
	public CrudHeaderButtonConfig saveShowIconOnly() { this.setShowSaveIconOnly(true); return this; }
	public CrudHeaderButtonConfig resetShowIconOnly() { this.setShowResetIconOnly(true); return this; }
	public CrudHeaderButtonConfig newShowIconOnly() { this.setShowNewIconOnly(true); return this; }
	public CrudHeaderButtonConfig deleteShowIconOnly() { this.setShowDeleteIconOnly(true); return this; }
	
	public CrudHeaderButtonConfig hideSave() { setShowSave(false); return this; }
	public CrudHeaderButtonConfig hideReset() { setShowReset(false); return this; }
	public CrudHeaderButtonConfig hideNew() { setShowNew(false); return this; }
	public CrudHeaderButtonConfig hideDelete() { setShowDelete(false); return this; }
}
