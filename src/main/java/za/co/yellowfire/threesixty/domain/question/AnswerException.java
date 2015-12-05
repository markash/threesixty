package za.co.yellowfire.threesixty.domain.question;

public class AnswerException extends Exception {
	private static final long serialVersionUID = 1L;
	
	public AnswerException() {
		super();
	}

	public AnswerException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AnswerException(String message, Throwable cause) {
        super(message, cause);
    }

	public AnswerException(String message) {
		super(message);
	}

	public AnswerException(Throwable cause) {
		super(cause);
	}
}
