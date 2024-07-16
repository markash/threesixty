package za.co.yellowfire.threesixty.domain.question;

public class RatingException extends AnswerException {
	private static final long serialVersionUID = 1L;
	
	public RatingException() {
		super();
	}

	public RatingException(String message, Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RatingException(String message, Throwable cause) {
        super(message, cause);
    }

	public RatingException(String message) {
		super(message);
	}

	public RatingException(Throwable cause) {
		super(cause);
	}
}
