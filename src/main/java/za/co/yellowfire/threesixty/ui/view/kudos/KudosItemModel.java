package za.co.yellowfire.threesixty.ui.view.kudos;

import java.io.Serializable;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;

import za.co.yellowfire.threesixty.domain.kudos.Kudos;
import za.co.yellowfire.threesixty.ui.component.ImageBuilder;

@SuppressWarnings("serial")
public class KudosItemModel implements Serializable {
	private Kudos proxied;
	//private boolean pictureRetrieved = false;
	
	public KudosItemModel(final Kudos proxied) {
		this.proxied = proxied;
	}
	
	public void setPicture(Resource resource) {}
	
	public Resource getPicture() { 
		if (proxied == null || proxied.getBadge() == null) {
			return ImageBuilder.BLANK_RESOURCE();
		}
		return new ExternalResource("http://localhost:8080/api/badge/image/" + proxied.getBadge().getId());
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
	public String getMessage() { return "<span style=\"vertical-align:top\">" + proxied.getMessage() + "</span>"; }
}