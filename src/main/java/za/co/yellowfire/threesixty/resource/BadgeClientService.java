package za.co.yellowfire.threesixty.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import za.co.yellowfire.threesixty.domain.kudos.BadgeProperties;

@Service
public class BadgeClientService {
	private static Logger LOG = LoggerFactory.getLogger(BadgeClientService.class);
	
	private final BadgeProperties properties;
	
	@Autowired
	public BadgeClientService(final BadgeProperties properties) {
		this.properties = properties;
	}
	
	public String getUri(final String imageId) {
		String url;
		
		if (properties != null && properties.getUri() != null) {
			if (properties.getUri().endsWith("/")) {
				url = properties.getUri() + imageId;
			} else {
				url = properties.getUri() + "/" + imageId;
			}
		} else {
			url = "http://localhost:8080/api/badge/image/";
		}
		
		LOG.info("Retrieving badge from web service call {}",  url);
		return url;
	}
}
