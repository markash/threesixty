package za.co.yellowfire.threesixty.ui.component;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Image;
import org.apache.commons.lang3.StringUtils;
import za.co.yellowfire.threesixty.domain.VisualEntity;

public class ImageBuilder {

	public static String IMAGE_NO_PHOTO = "img/no-photo-icon.png";
	public static String IMAGE_PROFILE_DEFAULT = "img/profile-pic-300px.jpg";
	
	public static Image build() {
		return build((String) null);
	}
	
	public static Image build(final String caption) {
		return new Image(caption);
	}
	
	public static Image build(final VisualEntity visualEntity) {
		return build(null, visualEntity);
	}
	
	public static Image build(final Resource resource) {
		return build(null, resource);
	}
	
	public static Image build(final String caption, final VisualEntity visualEntity) {
		return build(caption, visualEntity, null);
	}
	
	public static Image build(final String caption, final VisualEntity visualEntity, final String fallbackThemeImage) {
		Resource resource = null;
		
		if (visualEntity != null && visualEntity.hasPicture()) {
			resource = new ByteArrayStreamResource(visualEntity.getPictureContent(), visualEntity.getPictureName() + " .png");
		} else {
			resource = new ThemeResource(StringUtils.isBlank(fallbackThemeImage) ? IMAGE_NO_PHOTO : fallbackThemeImage);
		}
		
		return build(caption, resource);
	}
	
	public static Image build(final String caption, final Resource resource) {
		return new Image(caption, resource);
	}
	
	public static Resource BLANK_RESOURCE() {
		return new ThemeResource(IMAGE_NO_PHOTO);
	}
	
	public static Image BLANK() {
		return build(null, BLANK_RESOURCE());
	}
	
	public static Image PROFILE() {
		return build(null, new ThemeResource(IMAGE_PROFILE_DEFAULT));
	}
}
