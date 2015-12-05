package za.co.yellowfire.threesixty;

import java.io.Serializable;


/**
 * 
 * @author Mark P Ashworth
 * @version 0.0.1
 */
public class Response<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private RequestResult result;
	private T value;
	
	public Response(RequestResult result) {
		super();
		this.result = result;
		this.value = null;
	}
	
	public Response(RequestResult result, T value) {
		super();
		this.result = result;
		this.value = value;
	}
	
	public RequestResult getResult() { return result; }
	public T getValue() { return value; }
}
