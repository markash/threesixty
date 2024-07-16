package za.co.yellowfire.threesixty.resource;

import java.nio.charset.Charset;
import java.util.Optional;

import com.helger.commons.io.stream.StringInputStream;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Example;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//import za.co.yellowfire.threesixty.domain.GridFsClient;
//import za.co.yellowfire.threesixty.domain.kudos.Badge;
//import za.co.yellowfire.threesixty.domain.kudos.BadgeRepository;

@RestController
@RequestMapping("/api/badge/*")
public class BadgeWebService {
	private static final Logger LOG = LoggerFactory.getLogger(BadgeWebService.class);
//	private final GridFsClient client;
//	private final BadgeRepository repository;
	
//	@Autowired
//	public BadgeWebService(final BadgeRepository repository, final GridFsClient client) {
//		this.repository = repository;
//		this.client = client;
//	}
	
	@RequestMapping(value = "image/{id}", method = RequestMethod.GET) @ResponseBody
	public ResponseEntity<InputStreamResource> getImage(@PathVariable("id") @NotNull final String id) {
		
		//try {
//			Optional<Badge> badgeResult = repository.findOne(Example.of(Badge.EMPTY().withId(id)));
//			if (badgeResult.isEmpty()) {
//				throw new RuntimeException("Unable to find badge with id " + id);
//			}
//			Badge badge = badgeResult.get();

//			LOG.info("Retrieved badge image : {} has picture {}", badge.getId(),  badge.hasPicture());
			
//			GridFsResource file = badge.retrievePictureFile(client);
//			String contentType = file.getContentType();
//			return ResponseEntity.ok()
//		            .contentLength(file.contentLength())
//		            .contentType(MediaType.parseMediaType(contentType != null ? contentType : "image/png"))
//		            .body(new InputStreamResource(file.getInputStream()));

			return ResponseEntity.ok().body(new InputStreamResource(new StringInputStream("", Charset.defaultCharset())));

//		} catch (IOException e) {
//			throw new RuntimeException("Unable to load badge image from repository: " + e.getMessage());
//		}
	}
}
