package za.co.yellowfire.threesixty.ui.component.field;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("serial")
public class MStatsField extends CustomField<MStatsModel> {

	public final static String STYLE_WARNING = "stat-warning";
	public final static String STYLE_INFO = "stat-info";
	public final static String STYLE_ERROR = "stat-error";
	public final static String STYLE_SUCCESS = "stat-success";
	
	private Label statText = new Label();
	private Label statLabel = new Label();
	private Label statInfo = new Label("", ContentMode.HTML);
	private Label statPic = new Label("", ContentMode.HTML);
	
	private String styleName = null;
	private VerticalLayout layout = null;
	private MStatsModel value = null;

	public MStatsField() {
		this("0", "Some interesting event", "0% up from last week", VaadinIcons.ABACUS);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final VaadinIcons icon) {
		this(statistic, label, info, icon, null);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final VaadinIcons icon, final String styleName) {
		super.setValue(new MStatsModel());
		
		this.getValue().setStatistic(statistic);
		this.getValue().setStatisticLabel(label);
		this.getValue().setStatisticInfo(info);
		this.getValue().setStatisticIcon(icon);
		this.styleName = styleName;
	}

	public String getStatistic() { return this.getValue().getStatistic(); }
	public String getStatisticLabel() { return getValue().getStatisticLabel(); }	
	public String getStatisticInfo() { return getValue().getStatisticInfoAsHtml(); }
	public String getStatisticIcon() { return getValue().getStatisticIconAsHtml(); }
	
	public void setStatistic(String statistic) { 
		this.getValue().setStatistic(statistic); 
		updateStatistic();
	}
	
	public void setStatisticLabel(String statisticLabel) { 
		this.getValue().setStatisticLabel(statisticLabel);
		updateStatisticLabel();
	}
	
	public void setStatisticInfo(String statisticInfo) { 
		this.getValue().setStatisticInfo(statisticInfo); 
		updateStatisticInfo();
	}
	
	public void setStatisticIcon(VaadinIcons statisticIcon) {
		this.getValue().setStatisticIcon(statisticIcon);
		updateStatisticPic();
	}

	protected void updateStatistic() {
		if (this.statText != null) {
			this.statText.setValue(getStatistic());
		}
	}
	
	protected void updateStatisticLabel() {
		if (this.statLabel != null) {
			this.statLabel.setValue(getStatisticLabel());
		}
	}
	
	protected void updateStatisticInfo() {
		if (this.statInfo != null) {
			this.statInfo.setValue(getStatisticInfo());
		}
	}
	
	protected void updateStatisticPic() {
		if (this.statPic != null) {
			this.statPic.setValue(getStatisticIcon());
		}
	}
	
	@Override
	protected Component initContent() {
		layout = new VerticalLayout();
		layout.setStyleName("stats-panel");
		
		if (StringUtils.isNotBlank(styleName)) {
			layout.addStyleName(styleName);
		}
		
		statText = new Label(getStatistic());
		statText.setSizeUndefined();
		statText.setStyleName("stats-text");
		statText.addStyleName(ValoTheme.LABEL_H1);

		statLabel = new Label(getStatisticLabel());
		statLabel.setSizeUndefined();
		statLabel.setStyleName("stats-label");
		statLabel.addStyleName(ValoTheme.LABEL_H4);
		
		statInfo = new Label(getStatisticInfo(), ContentMode.HTML);
		statInfo.setStyleName("stats-info");
		
		statPic = new Label(getStatisticIcon(), ContentMode.HTML);
		statPic.setStyleName("stats-icon");

		layout.addComponents(statText, statLabel, statInfo, statPic);
		return layout;
	}

	private void updateContent() {
		updateStatistic();
		updateStatisticLabel();
		updateStatisticInfo();
		updateStatisticPic();
	}

	@Override
	protected void doSetValue(final MStatsModel value) {
		this.value = value;
	}

	@Override
	public MStatsModel getValue() {
		return this.value;
	}
}
