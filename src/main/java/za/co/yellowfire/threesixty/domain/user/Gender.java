package za.co.yellowfire.threesixty.domain.user;

import java.io.Serializable;

public class Gender implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	
	public Gender(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public int getId() { return id; }
	public String getName() { return name; }

	@Override
	public String toString() {
		return name == null ? "null" : name;
	}
}
