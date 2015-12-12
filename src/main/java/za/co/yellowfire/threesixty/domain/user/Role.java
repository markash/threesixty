package za.co.yellowfire.threesixty.domain.user;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;

@AccessType(Type.FIELD)
public class Role implements Persistable<String> {
	private static final long serialVersionUID = 1L;
	
	public static final String ROLE_ADMIN = "Admin";
	public static final String ROLE_USER = "User";
	public static final Role ADMIN = new Role(ROLE_ADMIN);
	public static final Role USER = new Role(ROLE_USER);
	
	@Id
	private String id;
	
	public Role() {}
	
	public Role(String id) {
		super();
		this.id = id;
	}
	
	@Override
	public String getId() { return null; }
	@Override
	public boolean isNew() { return false; }
	
	public boolean isAdmin() { return this.id.equals(ROLE_ADMIN); }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		
		Role other = (Role) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
