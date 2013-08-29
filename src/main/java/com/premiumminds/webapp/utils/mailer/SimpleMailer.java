package com.premiumminds.webapp.utils.mailer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SimpleMailer extends AbstractMailer {

	private Address from;
	
	public SimpleMailer(){}

	public SimpleMailer(String from) {
		try {
			this.from = convertStringToAddress(from);
		} catch (MailerException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected boolean isValidMailerConfiguration(Properties config) {
		return true;
	}

	@Override
	public void send(String to, String subject, String body)
			throws MailerException {
		this.send(to, null, subject, body);
	}

	@Override
	public void send(String to, Map<String, String> headers, String subject,
			String body) throws MailerException {
		Collection<String> tos = new ArrayList<String>();
		tos.add(to);
		this.send(tos, null, null, headers, subject, body);
	}

	@Override
	public void send(Collection<String> to, Collection<String> ccs,
			Collection<String> bccs, String subject, String body)
					throws MailerException {
		this.send(to, ccs, null, null, subject, body);
	}

	@Override
	public void send(Collection<String> to, Collection<String> ccs,
			Collection<String> bccs, Map<String, String> headers,
			String subject, String body) throws MailerException {

		if(!isValidConfiguration(getConfiguration())) throw new MailerException("Mailer was not configured");

		MimeMessage message = buildMessage(to, ccs, bccs, headers, subject, body);
		send(message);
	}

	@Override
	public void send(MimeMessage message) throws MailerException {
		Session session = Session.getDefaultInstance(getConfiguration());
		
		if(this.from != null) {
			try {
				message.setFrom(from);
			} catch (MessagingException e) {
				throw new MailerException(e);
			}
		}
		
		try {
			Transport tr = session.getTransport("smtp");
			if(getConfigurationProperty(Configuration.SMTP.AUTH, "true").equalsIgnoreCase("true")){
				tr.connect(getConfigurationProperty(Configuration.SMTP.USER), getConfigurationProperty(Configuration.SMTP.PASSWORD));
			} else {
				tr.connect();
			}
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
		} catch (MessagingException e) {
			throw new MailerException(e);
		}
	}

	protected MimeMessage buildMessage(Collection<String> to, Collection<String> ccs,
			Collection<String> bccs, Map<String, String> headers,
			String subject, String body) throws MailerException {

		Session session = Session.getDefaultInstance(getConfiguration());
		MimeMessage message = new MimeMessage(session);

		try {		
			if(isDebug()){

				for(InternetAddress address : convertStringsToAddressess(getList(getDebugAddress()))){
					message.addRecipient(javax.mail.Message.RecipientType.TO, address);
				}

			} else {
				for(String address : to){
					message.addRecipient(javax.mail.Message.RecipientType.TO, convertStringToAddress(address));
				}
				if(ccs!=null){
					for(String address : ccs){
						message.addRecipient(javax.mail.Message.RecipientType.CC, convertStringToAddress(address));
					}
				}
				if(bccs!=null){
					for(String address : bccs){
						message.addRecipient(javax.mail.Message.RecipientType.BCC, convertStringToAddress(address));
					}
				}
			}
			message.setSubject(subject);
			message.setContent(body, "text/plain; charset=\"utf-8\"");

			if(headers != null) {
				for(String key : headers.keySet()) {
					message.addHeader(key, headers.get(key));
					message.setHeader(key, headers.get(key));
				}
			}

			message.saveChanges();
		} catch (MailerException e) {
			throw new MailerException(e);
		} catch (MessagingException e) {
			throw new MailerException(e);
		}

		return message;
	}

}
