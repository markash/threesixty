package za.co.yellowfire.threesixty.ui.component.field;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.BeanItem;
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
	
	@PropertyId(MStatsModel.FIELD_STATISTIC)
	private MLabel statText = new MLabel();
	@PropertyId(MStatsModel.FIELD_STATISTIC_LABEL)
	private MLabel statDesc = new MLabel();
	@PropertyId(MStatsModel.FIELD_STATISTIC_INFO)
	private MLabel statInfo = new MLabel("", ContentMode.HTML);
	@PropertyId(MStatsModel.FIELD_STATISTIC_ICON)
	private MLabel statPic = new MLabel("", ContentMode.HTML);
	private String styleName = null;
	private VerticalLayout layout = null;
	
	private BeanItem<MStatsModel> statsFieldGroup;
	
	public MStatsField() {
		this("0", "Some interesting event", "0% up from last week", FontAwesome.ANDROID);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final FontAwesome icon) {
		this(statistic, label, info, icon, null);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final FontAwesome icon, final String styleName) {
		this.styleName = styleName;
	}

	public String getStatistic() { return this.getValue().getStatistic(); }
	public String getStatisticLabel() { return getValue().getStatisticLabel(); }
	
	public void setStatistic(String statistic) { this.getValue().setStatistic(statistic); }
	public void setStatisticLabel(String statisticLabel) { this.getValue().setStatisticLabel(statisticLabel); }
	public void setStatisticInfo(String statisticInfo) { this.getValue().setStatisticInfo(statisticInfo); }
	public void setStatisticIcon(FontAwesome statisticIcon) { this.getValue().setStatisticIcon(statisticIcon); }

	@Override
	protected Component initContent() {
		layout = new VerticalLayout();
		layout.setStyleName("stats-panel");
		
		if (StringUtils.isNotBlank(styleName)) {
			layout.addStyleName(styleName);
		}
		
		if (getValue() != null) {
			this.statsFieldGroup = new BeanItem<MStatsModel>(getValue());
		} else {
			this.statsFieldGroup = new BeanItem<MStatsModel>(new MStatsModel());
		}
		
		statText = new MLabel(this.statsFieldGroup.getItemProperty(MStatsModel.FIELD_STATISTIC)); 
		statText.setSizeUndefined();
		statText.setStyleName("stats-text");
		statText.addStyleName(ValoTheme.LABEL_H1);
		statText.setImmediate(true);
		
		statDesc = new MLabel(this.statsFieldGroup.getItemProperty(MStatsModel.FIELD_STATISTIC_LABEL)); 
		statDesc.setSizeUndefined();
		statDesc.setStyleName("stats-label");
		statDesc.addStyleName(ValoTheme.LABEL_H4);
		
		statInfo = new MLabel(this.statsFieldGroup.getItemProperty(MStatsModel.FIELD_STATISTIC_INFO), ContentMode.HTML); 
		statInfo.setStyleName("stats-info");
		
		statPic = new MLabel(this.statsFieldGroup.getItemProperty(MStatsModel.FIELD_STATISTIC_ICON), ContentMode.HTML); 
		statPic.setStyleName("stats-icon");

		layout.addComponents(statText, statDesc, statInfo, statPic);
		return layout;
	}

	
	@Override
	protected void setInternalValue(MStatsModel newValue) {
		super.setInternalValue(newValue);
		
		if (newValue != null && this.statsFieldGroup != null) {
			this.statText.getPropertyDataSource().setValue(newValue.getStatistic());
		}
	}

	@Override
	public Class<? extends MStatsModel> getType() { return MStatsModel.class; }
}
