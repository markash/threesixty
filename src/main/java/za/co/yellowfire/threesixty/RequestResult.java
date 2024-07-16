package za.co.yellowfire.threesixty;


public enum RequestResult {

	OK(200, "OK"),
	UNAUTHORIZED(401, "Unauthorized"),
	INVALID_USER_ID(500, "InvalidUserId"),
	INVALID_CHANGE_BY(500, "InvalidChangedBy")
	;
	
	
	private int code;
	private String description;
	
	private RequestResult(final int code, final String description) {
		this.code = code;
		this.description = description;
	}

	public int getCode() { return code; }
	public String getDescription() { return description; }
	public RequestResult setDescription(String description) { this.description = description; return this; }
}
