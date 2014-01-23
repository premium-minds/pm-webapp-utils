package com.premiumminds.webapp.utils.mailer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendGridMailer extends SimpleMailer {

	public SendGridMailer() {
	}

	public void send(String to, String subject, String body, String sendGridHeaders, MIMEMessageConfigurator config) throws MailerException {
		ArrayList<String> tos = new ArrayList<String>(1);
		tos.add(to);
		this.send(tos, null, null, null, subject, body, sendGridHeaders, config);
		return;
	}

	public void send(String to, String subject, String body, String sendGridHeaders) throws MailerException {
		this.send(to, null, subject, body, sendGridHeaders);
	}

	public void send(String to, Map<String, String> headers, String subject, String body, String sendGridHeaders) throws MailerException {
		Collection<String> tos = new ArrayList<String>(1);
		tos.add(to);
		this.send(tos, null, null, headers, subject, body, sendGridHeaders);
	}

	public void send(Collection<String> to, Collection<String> ccs, Collection<String> bccs, String subject, String body, String sendGridHeaders)
			throws MailerException {
		this.send(to, ccs, bccs, null, subject, body, sendGridHeaders);
	}

	public void send(Collection<String> to, Collection<String> ccs, Collection<String> bccs, Map<String, String> headers, String subject,
			String body, String sendGridHeaders) throws MailerException {
		MimeMessage message = buildMessage(to, ccs, bccs, headers, subject, body);

		this.send(message, sendGridHeaders);
		return;
	}

	public void send(Collection<String> to, Collection<String> ccs, Collection<String> bccs, Map<String, String> headers, String subject,
			String body, String sendGridHeaders, MIMEMessageConfigurator config) throws MailerException {
		MimeMessage message = buildMessage(to, ccs, bccs, headers, subject, body);
		MimeMessage toSend = null == config ? message : config.configure(message);

		this.send(toSend, sendGridHeaders);
		return;
	}

	@Override
	public void send(MimeMessage message) throws MailerException {
		this.send(message, null);
	}

	public void send(MimeMessage message, String sendGridHeaders) throws MailerException {
		Session session = Session.getDefaultInstance(getConfiguration());
		try {
			Transport tr = session.getTransport("smtp");
			/** SendGrid From */
			if (message.getFrom() == null && null != getConfigurationProperty(Configuration.SMTP.FROM)) {
				message.setFrom(new InternetAddress(getConfigurationProperty(Configuration.SMTP.FROM)));
			}

			if (getConfigurationProperty(Configuration.SMTP.AUTH, "true").equalsIgnoreCase("true")) {
				tr.connect(getConfigurationProperty(Configuration.SMTP.USER), getConfigurationProperty(Configuration.SMTP.PASSWORD));
			} else {
				tr.connect();
			}
			message = appendSendGridHeaders(message, sendGridHeaders);
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
		} catch (MessagingException e) {
			throw new MailerException(e);
		}
	}

	protected MimeMessage appendSendGridHeaders(MimeMessage message, String headers) throws MessagingException {
		message.addHeader("X-SMTPAPI", formatSendGridHeaders(headers));
		return message;
	}

	private String formatSendGridHeaders(String headers) {
		return headers;
	}

}
