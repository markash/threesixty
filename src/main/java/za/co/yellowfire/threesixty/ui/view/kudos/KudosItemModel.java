package za.co.yellowfire.threesixty.ui.view.kudos;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

import za.co.yellowfire.threesixty.domain.kudos.Kudos;
import za.co.yellowfire.threesixty.resource.BadgeClientService;
import za.co.yellowfire.threesixty.ui.component.ImageBuilder;

@SuppressWarnings("serial")
public class KudosItemModel implements Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(KudosItemModel.class);
	
	private Kudos proxied;
	private BadgeClientService service;
	
	public KudosItemModel(final BadgeClientService service, final Kudos proxied) {
		this.proxied = proxied;
		this.service = service;
	}
	
	public void setPicture(Resource resource) {}
	
	public Resource getPicture() { 
		if (proxied == null || proxied.getBadge() == null) {
			return ImageBuilder.BLANK_RESOURCE();
		}
		String uri = service.getUri(proxied.getBadge().getId());
		LOG.debug("Badge uri {}", uri);
		
		return new ExternalResource(uri, "image/svg");
		
//		if (!pictureRetrieved) {
//			try {
//				proxied.getBadge().retrievePicture(client);
//			} catch (IOException e) {
//				LOG.warn("Unable to retrieve kudos badge image {} : {}" + proxied.getBadge().getPictureName(), e.getMessage());
//			}
//		}
//		return proxied.getBadge().getPictureContent(); 
	}
	
	public void setMessage(final String message) {}
	public String getMessage() { 
		return "<div id=\"kudos-received-wrapper\" style=\"float: left; width: 100%;overflow-wrap: break-word;\">" +
				"<div id=\"kudos-received-date\" style=\"float: right; color: #666;\">" +
				getReceivedDate() +
				"</div>" +
				"<strong><i>" + 
				proxied.getBadge().getIdeal().getId() + 
				"</i> kudos received from " +
				getDonorName() +
				"</strong>" +
				"<p style=\"overflow-wrap: break-word;\">" + proxied.getMessage() + "</p>" +
				"</div>"; 
	}
	
	private String getDonorName() {
		return proxied.getDonor() != null && StringUtils.isNotBlank(proxied.getDonor().getFirstName()) ? proxied.getDonor().getFirstName() : "Unknown"; 
	}
	
	private String getReceivedDate() {
		if (proxied.getCreatedDate() != null) {
			return proxied.getCreatedDate().toString("dd MMM yyyy");
		}
		return "";
	}
}