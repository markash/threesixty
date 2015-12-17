package za.co.yellowfire.threesixty.ui;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DashboardConfig {

	private DashboardEventBus eventBus;
	
	@Bean
	public DashboardEventBus getEventBus() {
		if (eventBus == null) {
			this.eventBus = new DashboardEventBus();
		}
		return this.eventBus;
	}
}
