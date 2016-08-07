package za.co.yellowfire.threesixty.ui.component.field;

import java.io.Serializable;

import com.vaadin.server.FontAwesome;

public class MStatsModel implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public final static String FIELD_STATISTIC = "statistic";
	public final static String FIELD_STATISTIC_LABEL = "statisticLabel";
	public final static String FIELD_STATISTIC_INFO = "statisticInfoAsHtml";
	public final static String FIELD_STATISTIC_ICON = "statisticIconAsHtml";
	
	private String statistic = "";
	private String statisticLabel = "";
	private String statisticInfo = "";
	private String styleName = "";
	private FontAwesome statisticIcon = FontAwesome.ANDROID;
	
	public MStatsModel() { }
	
	public MStatsModel(String statistic, String statisticLabel, String statisticInfo, FontAwesome statisticIcon, String styleName) {
		this.statistic = statistic;
		this.statisticLabel = statisticLabel;
		this.statisticInfo = statisticInfo;
		this.statisticIcon = statisticIcon;
		this.styleName = styleName;
	}
	public String getStatistic() { return statistic; }
	public String getStatisticLabel() { return statisticLabel; }
	public String getStatisticInfo() { return statisticInfo; }
	public String getStyleName() { return styleName; }
	
	public String getStatisticInfoAsHtml() { return FontAwesome.INFO_CIRCLE.getHtml() + "  " + getStatisticInfo(); }
	public String getStatisticIconAsHtml() { return statisticIcon != null ? statisticIcon.getHtml() : null; }
	public FontAwesome getStatisticIcon() { return statisticIcon; }
	public void setStyleName(String styleName) { this.styleName = styleName; }
	
	public void setStatistic(String value) { this.statistic = value; }
	public void setStatisticLabel(String value) { this.statisticLabel = value; }
	public void setStatisticInfo(String value) { this.statisticInfo = value; }
	public void setStatisticIcon(FontAwesome statisticIcon) { this.statisticIcon = statisticIcon; }
	
	public <S> void updateWith(MStatsConverter<S> converter, S object) {
		if (converter == null) {
			throw new IllegalArgumentException("The converter object cannot be null");
		}
		
		updateWith(converter.convert(object));
	}
	
	public void updateWith(final MStatsModel model) {
		if (model != null) {
			if (model.getStatistic() != null)
				this.statistic =  model.getStatistic();
			
			if (model.getStatisticLabel() != null)
				this.statisticLabel =  model.getStatisticLabel();
			
			if (model.getStatisticInfo() != null)
				this.statisticInfo =  model.getStatisticInfo();
			
			if (model.getStatisticIcon() != null)
				this.statisticIcon =  model.getStatisticIcon();
		}
	}
}
