package za.co.yellowfire.threesixty.domain;

import org.springframework.dao.NonTransientDataAccessException;

@SuppressWarnings("serial")
public class InvalidUserException extends NonTransientDataAccessException {

	public InvalidUserException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public InvalidUserException(String msg) {
		super(msg);
	}
}
