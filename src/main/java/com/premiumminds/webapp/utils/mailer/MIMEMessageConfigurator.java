package com.premiumminds.webapp.utils.mailer;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

public abstract class MIMEMessageConfigurator {
	

	abstract protected void doConfig(MimeMessage m) throws MessagingException;

	public MimeMessage configure(MimeMessage message) {
		try {
			doConfig(message);
			message.saveChanges();
		}
		catch (MessagingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return message;
	}

}
