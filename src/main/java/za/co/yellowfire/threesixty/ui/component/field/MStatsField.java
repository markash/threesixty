package za.co.yellowfire.threesixty.ui.component.field;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class MStatsField extends VerticalLayout {
	
	public final static String STYLE_WARNING = "stat-warning";
	public final static String STYLE_INFO = "stat-info";
	public final static String STYLE_ERROR = "stat-error";
	public final static String STYLE_SUCCESS = "stat-success";
	
	private final static String FIELD_STATISTIC = "statistic";
	private final static String FIELD_STATISTIC_LABEL = "statisticLabel";
	private final static String FIELD_STATISTIC_INFO = "statisticInfoAsHtml";
	private final static String FIELD_STATISTIC_ICON = "statisticIconAsHtml";
	
	private BeanItem<MStatsField> statsFieldGroup;
	
	private String statistic;
	private String statisticLabel;
	private String statisticInfo;
	private FontAwesome statisticIcon;
	
	public MStatsField() {
		this("0", "Some interesting event", "0% up from last week", FontAwesome.ANDROID);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final FontAwesome icon) {
		this(statistic, label, info, icon, null);
	}
	
	public MStatsField(final String statistic, final String label, final String info, final FontAwesome icon, final String styleName) {
		setStyleName("stats-panel");
		
		if (StringUtils.isNotBlank(styleName)) {
			addStyleName(styleName);
		}
		
		this.statistic = statistic;
		this.statisticLabel = label;
		this.statisticInfo = info;
		this.statisticIcon = icon;
		this.statsFieldGroup = new BeanItem<>(this);
		
		MLabel statText = new MLabel(this.statsFieldGroup.getItemProperty(FIELD_STATISTIC)); 
		statText.setSizeUndefined();
		statText.setStyleName("stats-text");
		statText.addStyleName(ValoTheme.LABEL_H1);
		
		MLabel statDesc = new MLabel(this.statsFieldGroup.getItemProperty(FIELD_STATISTIC_LABEL)); 
		statDesc.setSizeUndefined();
		statDesc.setStyleName("stats-label");
		statDesc.addStyleName(ValoTheme.LABEL_H4);
		
		MLabel statInfo = new MLabel(this.statsFieldGroup.getItemProperty(FIELD_STATISTIC_INFO), ContentMode.HTML); 
		statInfo.setStyleName("stats-info");
		
		MLabel statPic = new MLabel(this.statsFieldGroup.getItemProperty(FIELD_STATISTIC_ICON), ContentMode.HTML); 
		statPic.setStyleName("stats-icon");
		
		
		addComponents(statText, statDesc, statInfo, statPic);
	}

	public String getStatistic() {
		return statistic;
	}

	public void setStatistic(String statistic) {
		this.statistic = statistic;
	}

	public String getStatisticLabel() {
		return statisticLabel;
	}

	public void setStatisticLabel(String statisticLabel) {
		this.statisticLabel = statisticLabel;
	}

	public String getStatisticInfoAsHtml() {
		return FontAwesome.INFO_CIRCLE.getHtml() + "  " + statisticInfo;
	}

	public void setStatisticInfo(String statisticInfo) {
		this.statisticInfo = statisticInfo;
	}

	public String getStatisticIconAsHtml() {
		return statisticIcon.getHtml();
	}

	public FontAwesome getStatisticIcon() {
		return statisticIcon;
	}

	public void setStatisticIcon(FontAwesome statisticIcon) {
		this.statisticIcon = statisticIcon;
	}
	
	
}
