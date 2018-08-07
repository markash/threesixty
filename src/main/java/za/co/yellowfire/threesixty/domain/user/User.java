package za.co.yellowfire.threesixty.domain.user;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import za.co.yellowfire.threesixty.domain.GridFsClient;
import za.co.yellowfire.threesixty.domain.organization.Organization;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;


@AccessType(Type.FIELD)
public final class User implements Auditable<User, Serializable>, UserDetails {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(User.class);

	public static final String FIELD_ID = "id";
	public static final String FIELD_ROLE = "role";
	public static final String FIELD_EMAIL = "email";
	public static final String FIELD_FIRST_NAME = "firstName";
	public static final String FIELD_LAST_NAME = "lastName";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_GENDER = "gender";
	public static final String FIELD_LOCATION = "location";
	public static final String FIELD_PHONE = "phone";
	public static final String FIELD_WEBSITE = "website";
	public static final String FIELD_BIO = "bio";
	public static final String FIELD_ACTIVE = "active";

	public static final String USER_ADMIN = "admin";
	public static final String USER_ADMIN_PASSWORD = "password";
	public static final String PROPERTY_FULL_NAME = "fullName";
	
	@Id
	private String id;
	@NotNull(message = "{user.password.NotNull.message}")
	private String password;
	private Role role;
	@NotNull(message = "{user.firstName.NotNull.message}")
    private String firstName;
	@NotNull(message = "{user.lastName.NotNull.message}")
    private String lastName;
    private String title;
    private String gender;
    @Email @NotEmpty
    private String email;
    @DBRef
    private Country location;
    private String phone;
    private Integer newsletterSubscription;
    private String website;
    private String bio;
    private String image;
    @DBRef
    private User reportsTo;
    @DBRef
    private Organization department;
    @DBRef
    private Position position;
    @DBRef
    private User createdBy;
    @DBRef
	private User modifiedBy;
	private DateTime createdDate;
	private DateTime modifiedDate;
	private boolean active = true;
	private boolean passwordChange = true;
	
    @Transient
    private byte[] imageContent = new byte[0];
    
    public User() {}
    
    public User(String id, String password, Role role) {
		super();
		this.id = id;
		this.password = password;
		this.role = role;
	}
    
    public static User EMPTY() {
		return new User();
	}
    
	@Override
	public String getId() { return this.id; }
    public void setId(String id) { this.id = id; }
    
    public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	
	public boolean isAdmin() { return this.role != null && this.role.isAdmin(); }
	
	public String getEmail() { return email; }
    public void setEmail(final String email) { this.email = email; }
    
    public Country getLocation() { return location; }
    public void setLocation(final Country location) { this.location = location; }
    
    public String getPhone() { return phone; }
    public void setPhone(final String phone) { this.phone = phone; }

    public Integer getNewsletterSubscription() { return newsletterSubscription; }
    public void setNewsletterSubscription(final Integer newsletterSubscription) { this.newsletterSubscription = newsletterSubscription; }

    public String getWebsite() { return website; }
    public void setWebsite(final String website) { this.website = website; }

    public String getBio() { return bio; }
    public void setBio(final String bio) { this.bio = bio; }

    public boolean isMale() { return this.gender != null && this.gender.equalsIgnoreCase("MALE"); }
    
    public String getGender() { return this.gender; }
    public void setGender(final String gender) { this.gender = gender; }
    
    public String getTitle() { return title; }
    public void setTitle(final String title) { this.title = title; }

    public Role getRole() { return role; }
    public void setRole(final Role role) { this.role = role; }

    public Organization getDepartment() { return department; }
	public void setDepartment(Organization department) { this.department = department; }

	public User getReportsTo() { return reportsTo; }
	public void setReportsTo(User reportsTo) { this.reportsTo = reportsTo; }

	public Position getPosition() { return position; }
	public void setPosition(Position position) { this.position = position; }

	public String getFirstName() { return firstName; }
    public void setFirstName(final String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(final String lastName) { this.lastName = lastName; }
	
    public String getFullName() { 
    	return (!StringUtils.isBlank(lastName) ? lastName  + ", " : "") +
    			(!StringUtils.isBlank(firstName) ? firstName : "");    	
    }
    
    public boolean isActive() { return active; }
	public void setActive(boolean active) { this.active = active; }

	public boolean isPasswordChangeRequired() { return passwordChange; }
	public void setPasswordChangeRequired(final boolean passwordChange) { this.passwordChange = passwordChange; }
	
	public byte[] getPictureContent() { return this.imageContent; };
    public String getPictureName() { return this.image; }
    
    public void retrievePicture(final GridFsClient client) throws IOException {
    	if (client != null && image != null) {
    		this.imageContent = client.retrieveFileContents(image);
    	}
    }
    
    public void retrievePictureSilently(final GridFsClient client) {
    	try {
	    	if (client != null && image != null) {
	    		this.imageContent = client.retrieveFileContents(image);
	    	}
    	} catch (IOException e) {
    		LOG.warn("Unable to retrieve the profile picture for user {} : {}", getId(), e.getMessage());
    	}
    }
    
    public boolean hasPicture() { return this.image != null && this.imageContent.length > 0; }
    
    public void setPicture(final File file) throws IOException {
    	if (file == null) { return; }
    	
    	String fileName = file.getName();
    	String extension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    	
    	this.image = "user-" + getId() + "." + extension;
    	this.imageContent = FileUtils.readFileToByteArray(file);
    }
    
    public void storePicture(final GridFsClient client) throws IOException {
    	if (client == null) { return; }
    	if (hasPicture()) {
    		client.storeFile(this.imageContent, this.image);
    	}
    }
    
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

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singletonList(isAdmin() ? new SimpleGrantedAuthority("ROLE_ADMIN") : new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getUsername() { return this.id; }

	public boolean isAccountNonExpired() {
		return true;
	}

	public boolean isAccountNonLocked() {
		return true;
	}

	public boolean isCredentialsNonExpired() {
		return true;
	}

	public boolean isEnabled() {
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

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
		return getFullName();
	}
}
