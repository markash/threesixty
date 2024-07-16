package za.co.yellowfire.threesixty.domain.user;

import java.io.Serializable;

public class Salutation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String name;
	
	public Salutation(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name == null ? "null" : name;
	}
}
