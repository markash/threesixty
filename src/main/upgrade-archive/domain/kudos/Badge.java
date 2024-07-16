package za.co.yellowfire.threesixty.domain.kudos;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotNull;
import org.apache.commons.io.FileUtils;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Transient;

import za.co.yellowfire.threesixty.domain.AbstractAuditable;
import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.VisualEntity;

@Entity
@AccessType(Type.FIELD)
public class Badge extends AbstractAuditable implements VisualEntity  {
	private static final String IMAGE_PREFIX = "badge_";

	public static Badge EMPTY() { return new Badge(); }

	/** The description of the badge */
	private String description;
	/** The face-value of the badge */
	@NotNull @Min(value = 0) @Max(value = 1000)
	private Integer value = 0;
	@ManyToOne
	@JoinColumn(name = "idealId")
	private Ideal ideal;
	private String motivation;
	
	private String image;
	@Transient
    private byte[] imageContent = new byte[0];

    public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	
	public Integer getValue() { return this.value; }
	public void setValue(final Integer value) { this.value = value != null ? value : 0; }
	
	public Ideal getIdeal() { return ideal; }
	public void setIdeal(Ideal ideal) { this.ideal = ideal; }
	
	public String getMotivation() { return motivation; }
	public void setMotivation(String motivation) { this.motivation = motivation; }
	
	public byte[] getPictureContent() { return this.imageContent; };
    public String getPictureName() { return this.image; }
    
    public void retrievePicture(final GridFsClient client) throws IOException {
    	if (client != null && image != null) {
    		this.imageContent = client.retrieveFileContents(image);
    	}
    }
    
//    public GridFsResource retrievePictureFile(final GridFsClient client) throws IOException {
//    	if (client != null && image != null) {
//    		return client.retrieveResource(image);
//    	}
//    	return null;
//    }
    
    public boolean hasPicture() { return this.image != null && this.imageContent.length > 0; }
    
    public void setPicture(final File file) throws IOException {
    	if (file == null) { return; }
    	
    	String fileName = file.getName();
    	String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    	
    	this.image = IMAGE_PREFIX + getId() + "." + extension;
    	this.imageContent = FileUtils.readFileToByteArray(file);
    }
    
    public void storePicture(final GridFsClient client) throws IOException {
    	if (client == null) { return; }
    	if (hasPicture()) {
    		client.storeFile(this.imageContent, this.image);
    	}
    }

	public Badge withId(final String id) {
		setId(id);
		return this;
	}

	@Override
	public String toString() {
		return Objects.toString(getId()) + " ($" + this.value + ")";
	}	
}
