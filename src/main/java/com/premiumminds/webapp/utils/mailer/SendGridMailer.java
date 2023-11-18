/**
 * Copyright (C) 2014 Premium Minds.
 *
 * This file is part of pm-webapp-utils.
 *
 * pm-webapp-utils is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * pm-webapp-utils is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with pm-webapp-utils. If not, see <http://www.gnu.org/licenses/>.
 */
package com.premiumminds.webapp.utils.mailer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

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
