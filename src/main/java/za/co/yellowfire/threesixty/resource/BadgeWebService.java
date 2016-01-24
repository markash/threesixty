package za.co.yellowfire.threesixty.resource;

import java.io.IOException;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.kudos.Badge;
import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;

@RestController
@RequestMapping("/api/badge/*")
public class BadgeWebService {
	private static Logger LOG = LoggerFactory.getLogger(BadgeWebService.class);
	private final GridFsClient client;
	private final BadgeRepository repository;
	
	@Autowired
	public BadgeWebService(final BadgeRepository repository, final GridFsClient client) {
		this.repository = repository;
		this.client = client;
	}
	
	@RequestMapping(value = "image/{id}", method = RequestMethod.GET, produces = "image/png")
	public @ResponseBody byte[] getImage(@PathVariable("id") @NotNull final String id) {
		
		try {
			Badge badge = repository.findOne(id);
			if (badge == null) {
				throw new RuntimeException("Unable to find badge with id " + id);
			}
			
			badge.retrievePicture(client);
			
			LOG.info("Retrieved badge image : {} has picture {}", badge.getId(),  badge.hasPicture());
			
			return badge.getPictureContent();
		} catch (IOException e) {
			throw new RuntimeException("Unable to load badge image from repository: " + e.getMessage());
		}
	}
}
