package za.co.yellowfire.threesixty.domain.mail;

import org.springframework.context.ApplicationEvent;

public class MailingEvent extends ApplicationEvent {

    private final String action;
    private final String message;

    MailingEvent(
            final Object source,
            final String action,
            final String message) {

        super(source);

        this.action = action;
        this.message = message;
    }

    public String getAction() { return action; }

    public String getMessage() { return message; }
}
