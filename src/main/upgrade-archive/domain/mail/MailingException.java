package za.co.yellowfire.threesixty.domain.mail;

@SuppressWarnings("serial")
public class MailingException extends Exception {

	public MailingException() {
		super();
	}

	public MailingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MailingException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailingException(String message) {
		super(message);
	}

	public MailingException(Throwable cause) {
		super(cause);
	}
}
