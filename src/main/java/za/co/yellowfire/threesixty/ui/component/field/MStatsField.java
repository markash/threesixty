package za.co.yellowfire.threesixty.ui.component.field;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MStatsField extends CustomField<MStatsModel> {
	
	public final static String STYLE_WARNING = "stat-warning";
	public final static String STYLE_INFO = "stat-info";
	public final static String STYLE_ERROR = "stat-error";
	public final static String STYLE_SUCCESS = "stat-success";
	
	private MLabel statText = new MLabel();
	private MLabel statLabel = new MLabel();
	private MLabel statInfo = new MLabel("", ContentMode.HTML);
	private MLabel statPic = new MLabel("", ContentMode.HTML);
	
	private String styleName = null;
	private VerticalLayout layout = null;
	
	public MStatsField() {
		this("0", "Some interesting event", "0% up from last week", FontAwesome.ANDROID);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final FontAwesome icon) {
		this(statistic, label, info, icon, null);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final FontAwesome icon, final String styleName) {
		super.setInternalValue(new MStatsModel());
		
		this.getInternalValue().setStatistic(statistic);
		this.getInternalValue().setStatisticLabel(label);
		this.getInternalValue().setStatisticInfo(info);
		this.getInternalValue().setStatisticIcon(icon);
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
	
	public void setStatisticIcon(FontAwesome statisticIcon) { 
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
		
		statText = new MLabel(getStatistic()); 
		statText.setSizeUndefined();
		statText.setStyleName("stats-text");
		statText.addStyleName(ValoTheme.LABEL_H1);
		statText.setImmediate(false);
		
		statLabel = new MLabel(getStatisticLabel()); 
		statLabel.setSizeUndefined();
		statLabel.setStyleName("stats-label");
		statLabel.addStyleName(ValoTheme.LABEL_H4);
		
		statInfo = new MLabel(getStatisticInfo(), ContentMode.HTML); 
		statInfo.setStyleName("stats-info");
		
		statPic = new MLabel(getStatisticIcon(), ContentMode.HTML); 
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
	protected void setInternalValue(MStatsModel newValue) {
		/* Update the internal value */
		super.setInternalValue(newValue);
		/* Update the displayed values */
		updateContent();
	}

	@Override
	public Class<? extends MStatsModel> getType() { return MStatsModel.class; }
}
