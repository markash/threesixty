package za.co.yellowfire.threesixty.resource;

import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Service;

@Service
public class BadgeClientService {
	private static Logger LOG = LoggerFactory.getLogger(BadgeClientService.class);
	
	private final ServerProperties properties;
	
	@Autowired
	public BadgeClientService(final ServerProperties properties) {
		this.properties = properties;
	}
	
	public String getUri(final String imageId) {
		
		String hostName = "localhost";
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {/*IGNORE*/}
		
		String uri = "http://" + hostName + ":" + properties.getPort() + "/api/badge/image/" + imageId;
		LOG.info("Badge uri : {}", uri);
		
		return uri;
	}
}
