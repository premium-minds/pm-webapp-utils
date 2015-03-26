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
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class SimpleMailer extends AbstractMailer {

	private Address from;
	private String[] filenames;
	
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
		this.send(to, ccs, bccs, null, subject, body);
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
			
			Multipart mp = new MimeMultipart();
			
			BodyPart messageBodyText = new MimeBodyPart();  
			messageBodyText.setContent(body, "text/plain; charset=\"utf-8\"");  
			
			mp.addBodyPart(messageBodyText);
			
			if(null != getAttachmentsFiles()){
				  
				for (String filename : getAttachmentsFiles()) {
					 DataSource source = new FileDataSource(filename);
					
					BodyPart messageAttachment = new MimeBodyPart();
					 
					 messageAttachment.setDataHandler(new DataHandler(source));
					 messageAttachment.setFileName(filename);
					
					 mp.addBodyPart(messageAttachment);
				}
			}
			
			message.setContent(mp);
					
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
	
	private String[]  getAttachmentsFiles() {
		return this.filenames ;
	}

	public void attachFile(String[] files) {
		this.filenames = files;
	}

}
