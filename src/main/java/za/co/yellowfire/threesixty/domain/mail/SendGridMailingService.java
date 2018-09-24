package za.co.yellowfire.threesixty.domain.mail;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;
import org.springframework.context.ApplicationContext;
import za.co.yellowfire.threesixty.domain.user.notification.NotificationAction;

public class SendGridMailingService {

	private final ApplicationContext context;
	private final MailingProperties properties;
	
	public SendGridMailingService(
			final ApplicationContext context,
			final MailingProperties properties) {

		this.context = context;
		this.properties = properties;
	}
	
	public void send() throws MailingException {
		if (properties == null) {
			context.publishEvent(
					new MailingEvent(
							this,
							NotificationAction.Mailing.name(),
							"The mailing properties is not set"));
			throw new MailingException("The mailing properties is not set");
		}
		if (!properties.isKeyConfigured()) {
			context.publishEvent(
					new MailingEvent(
							this,
							NotificationAction.Mailing.name(),
							"The mailing key is not configured"));
			throw new MailingException("The mailing key is not configured");
		}

		try {
			SendGrid sendGrid = new SendGrid(properties.getKey());
			SendGrid.Email email = new SendGrid.Email();
			email.addTo("mp.ashworth@gmail.com");
			email.setFrom("mp.ashworth@gmail.com");
			email.setSubject("Hello World");
			email.setTemplateId("167bbd99-dba1-4066-9f03-bd7ebadbac68");
			email.setHtml("<strong>My first email with SendGrid Java!</strong>");

	        SendGrid.Response response = sendGrid.send(email);

	        this.context.publishEvent(
	        		new MailingEvent(
	        				this,
							NotificationAction.Mailing.name(),
							"Email sent to " + String.join(",", email.getTos()) + " with response " + response.getMessage()));
	      } catch (SendGridException e) {

			this.context.publishEvent(
					new MailingEvent(
							this,
							NotificationAction.Mailing.name(),
							e.getMessage()));

			throw new MailingException("Unable to send email", e);
	      }
	}
}
