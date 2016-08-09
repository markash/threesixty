package za.co.yellowfire.threesixty.domain.kudos;

import java.io.File;
import java.io.IOException;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.VisualEntity;
import za.co.yellowfire.threesixty.domain.user.User;

@AccessType(Type.FIELD)
public class Badge implements Auditable<User, String>, VisualEntity {
	private static final long serialVersionUID = 1L;
	private static final String IMAGE_PREFIX = "badge_";
	public static final Badge EMPTY() { return new Badge(); }
	
	public static final String FIELD_VALUE = "value";
	
	@Id @NotNull
	private String id;
	/** The description of the badge */
	private String description;
	/** The face-value of the badge */
	@NotNull @Min(value = 0) @Max(value = 1000)
	private Integer value = 0;
	@DBRef
	private Ideal ideal;
	private String motivation;
	
	private String image;
	@Transient
    private byte[] imageContent = new byte[0];
	
	private boolean active = true;
	@DBRef
	private User createdBy;
	@DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	
	@Override
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
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
    
    public GridFsResource retrievePictureFile(final GridFsClient client) throws IOException {
    	if (client != null && image != null) {
    		return client.retrieveResource(image);
    	}
    	return null;
    }
    
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
    
	public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	@Override
	public boolean isNew() { return StringUtils.isBlank(this.id); }
	
	@Override
	public User getCreatedBy() { return this.createdBy; }
	
	@Override
	public void setCreatedBy(User createdBy) { this.createdBy = createdBy; }
	
	@Override
	public DateTime getCreatedDate() { return this.createdDate; }
	
	@Override
	public void setCreatedDate(DateTime creationDate) { this.createdDate = creationDate; }
	
	@Override
	public User getLastModifiedBy() { return this.modifiedBy; }
	
	@Override
	public void setLastModifiedBy(User lastModifiedBy) { this.modifiedBy = lastModifiedBy; }
	
	@Override
	public DateTime getLastModifiedDate() { return this.modifiedDate; }
	
	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) { this.modifiedDate = lastModifiedDate; }
	
	public void auditChangedBy(final User user) {
		if (isNew()) {
			setCreatedBy(user);
			setCreatedDate(DateTime.now());
		} else {
			setLastModifiedBy(user);
			setLastModifiedDate(DateTime.now());
		}
	}
	@Override
	public String toString() {
		return this.id + " ($" + this.value + ")";
	}	
}
