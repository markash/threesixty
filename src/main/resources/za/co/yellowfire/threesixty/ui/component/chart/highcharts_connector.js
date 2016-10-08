window.za_co_yellowfire_threesixty_ui_component_chart_TestChart = function() {
	this.onStateChange = function() {
		// read state
		var domId = this.getState().domId;
		var hcjs = this.getState().hcjs;

		// evaluate high charts JS which needs to define var "options"
		eval(hcjs);

		// set chart context
		$('#' + domId).highcharts(options)
	}
}