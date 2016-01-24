package za.co.yellowfire.threesixty.domain.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

public class SendGridMailingService {

	private final MailingProperties properties;
	
	public SendGridMailingService(final MailingProperties properties) {
		this.properties = properties;
	}
	
	public void send() throws MailingException {
		if (properties == null) { throw new MailingException("The mailing properties is not set"); }
		if (!properties.isKeyConfigured()) { throw new MailingException("The mailing key is not configured"); }
		
		//SendGrid sendGrid = new SendGrid("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");
		SendGrid sendGrid = new SendGrid(properties.getKey());
		SendGrid.Email email = new SendGrid.Email();
	    email.addTo("mp.ashworth@gmail.com");
	    email.setFrom("mp.ashworth@gmail.com");
	    email.setSubject("Hello World");
	    //email.setText();
	    email.setTemplateId("167bbd99-dba1-4066-9f03-bd7ebadbac68");
	    email.setHtml("<strong>My first email with SendGrid Java!</strong>");
	    
	    try {
	        SendGrid.Response response = sendGrid.send(email);
	        System.out.println(response.getMessage());
	      }
	      catch (SendGridException e) {
	        System.err.println(e);
	      }
	}
}
