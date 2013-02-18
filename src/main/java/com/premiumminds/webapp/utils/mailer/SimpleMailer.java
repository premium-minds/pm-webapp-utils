package com.premiumminds.webapp.utils.mailer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

public class SimpleMailer extends AbstractMailer {

	public SimpleMailer(){}
	
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
		
		Session session = Session.getDefaultInstance(getConfiguration());
		MimeMessage message = new MimeMessage(session);
		
		try {
			if(isDebug()){
				for(InternetAddress address : convertStringsToAddressess(getList(getDebugAddress()))){
					message.addRecipient(RecipientType.TO, address);
				}
			} else {
				for(String address : to){
					message.addRecipient(RecipientType.TO, convertStringToAddress(address));
				}
				if(ccs!=null){
					for(String address : ccs){
						message.addRecipient(RecipientType.CC, convertStringToAddress(address));
					}
				}
				if(bccs!=null){
					for(String address : bccs){
						message.addRecipient(RecipientType.BCC, convertStringToAddress(address));
					}
				}
			}
			message.setSubject(subject);
			message.setContent(body, "text/plain");
			
			if(headers != null) {
				for(String key : headers.keySet()) {
					message.addHeader(key, headers.get(key));
					message.setHeader(key, headers.get(key));
				}
			}
			
			message.saveChanges();
			send(message);
		} catch (AddressException e) {
			throw new MailerException(e);
		} catch (MessagingException e) {
			throw new MailerException(e);
		}
	}

	@Override
	public void send(MimeMessage message) throws MailerException {
		Session session = Session.getDefaultInstance(getConfiguration());
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
	
}
