package za.co.yellowfire.threesixty.domain.user;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Auditable;
import org.springframework.data.mongodb.core.mapping.DBRef;


@AccessType(Type.FIELD)
public final class User implements Auditable<User, String> {
	private static final long serialVersionUID = 1L;
	
	public static final String USER_ADMIN = "admin";
	public static final String USER_ADMIN_PASSWORD = "password";
	
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

    public String getFirstName() { return firstName; }
    public void setFirstName(final String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(final String lastName) { this.lastName = lastName; }
	
	@Override
	public boolean isNew() {
		return false;
	}
	@Override
	public User getCreatedBy() {
		return null;
	}
	@Override
	public void setCreatedBy(User createdBy) {
	}
	
	@Override
	public DateTime getCreatedDate() {
		return null;
	}
	@Override
	public void setCreatedDate(DateTime creationDate) {

		
	}
	@Override
	public User getLastModifiedBy() {
		return null;
	}
	@Override
	public void setLastModifiedBy(User lastModifiedBy) {

		
	}
	@Override
	public DateTime getLastModifiedDate() {

		return null;
	}
	@Override
	public void setLastModifiedDate(DateTime lastModifiedDate) {

		
	}
}
