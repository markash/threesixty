package za.co.yellowfire.threesixty.domain.mail;

import com.sendgrid.*;
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
//			TrackingSettings trackingSettings = new TrackingSettings();
//			trackingSettings.getOpenTrackingSetting().setEnable(true);

			Email to = new Email("mp.ashworth@gmail.com", "Mark Ashworth");
			Email from = new Email("mp.ashworth@gmail.com", "ThreeSixty Performance");
			Content content = new Content("text/html", "Test");

			Mail mail = new Mail(from, "ThreeSixty Assessment", to, content);
			mail.setTemplateId("d-11fbd64216564c4ebbc24d4349bfd4c1");


			DynamicPersonalization personalization = new DynamicPersonalization();
			personalization.addTo(to);
			personalization.addDynamicTemplateData("fullName", "Mark P Ashworth");
			personalization.addDynamicTemplateData("stage", "Final");

			mail.addPersonalization(personalization);

			SendGrid sendGrid = new SendGrid(properties.getKey());
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			Response response = sendGrid.api(request);
			System.out.println(response.getStatusCode());
			System.out.println(response.getBody());
			System.out.println(response.getHeaders());

	        this.context.publishEvent(
	        		new MailingEvent(
	        				this,
							NotificationAction.Mailing.name(),
							"Email sent to " + String.join(",", to.getEmail()) + " with response " + response.getStatusCode()));
	      } catch (Exception e) {

			this.context.publishEvent(
					new MailingEvent(
							this,
							NotificationAction.Mailing.name(),
							e.getMessage()));

			throw new MailingException("Unable to send email", e);
	      }
	}
}
