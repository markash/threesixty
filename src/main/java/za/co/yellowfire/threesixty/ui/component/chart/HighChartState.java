package za.co.yellowfire.threesixty.ui.component.chart;

import com.vaadin.shared.ui.JavaScriptComponentState;

/**
 * State of the chart which is transferred to the web browser whenever a property changed.
 *
 * @author Stefan Endrullis
 */
public class HighChartState extends JavaScriptComponentState {  
	private static final long serialVersionUID = 1L;
	
	public String domId;
	public String hcjs;
}