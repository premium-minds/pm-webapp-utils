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

import com.premiumminds.webapp.utils.mailer.AbstractMailer.Configuration.SMTP;
import jakarta.mail.Message.RecipientType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Address;
import jakarta.mail.BodyPart;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMailer extends AbstractMailer {
	
	private static final Logger log = LoggerFactory.getLogger(SimpleMailer.class);

	private Address from;
	private String[] filenames;
	private String bodyPartType = "text/plain; charset=\"utf-8\"";
	
	public SimpleMailer(){}

	public SimpleMailer(String from) {
		try {
			this.from = convertStringToAddress(from);
		} catch (MailerException e) {
			e.printStackTrace();
		}
	}
	
	Map<String,String> fileContentTypes = new HashMap<String,String>();
	
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
			if(getConfigurationProperty(SMTP.AUTH, "true").equalsIgnoreCase("true")){
				tr.connect(getConfigurationProperty(SMTP.USER), getConfigurationProperty(SMTP.PASSWORD));
			} else {
				tr.connect();
			}
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
		} catch (MessagingException e) {
			throw new MailerException(e);
		}
	}

	protected MimeMessage buildMessage(Collection<String> tos, Collection<String> ccs,
			Collection<String> bccs, Map<String, String> headers,
			String subject, String body) throws MailerException {

		Session session = Session.getDefaultInstance(getConfiguration());
		MimeMessage message = new MimeMessage(session);

		try {
			if (isDebug()) {
				for (InternetAddress address : convertStringsToAddressess(getList(getDebugAddress()))) {
					message.addRecipient(RecipientType.TO, address);
				}
			} else {
				for(String address : tos){
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
			
			Multipart mp = new MimeMultipart();
			
			BodyPart messageBodyText = new MimeBodyPart();  
			messageBodyText.setContent(body, getBodyPartType());  
			
			mp.addBodyPart(messageBodyText);
			
			if (null != getAttachmentsFiles()) {

				for (String filename : getAttachmentsFiles()) {
					File file = new File(filename);
					DataSource source = new FileDataSource(file);

					BodyPart messageAttachment = new MimeBodyPart();
					messageAttachment.setDataHandler(new DataHandler(source));
					
					messageAttachment.setFileName(file.getName());
					messageAttachment.setDisposition(Part.ATTACHMENT);

					String contentType = getAttachmentContentType(source.getContentType(), filename);
					messageAttachment.addHeader("Content-Type", contentType);

					mp.addBodyPart(messageAttachment);
				}
			}
			
			message.setContent(mp);

			if (headers != null) {
				for (String key : headers.keySet()) {
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
	
	protected String[] getAttachmentsFiles() {
		return this.filenames;
	}

	public void attachFile(String[] files) {
		this.filenames = files;
	}

	public String getBodyPartType() {
		return this.bodyPartType;
	}

	public void setBodyPartType(String bodyPartType) {
		this.bodyPartType = bodyPartType;
	}
	
	public String getAttachmentContentType(String defaultType, String filename) {
		if (filename!=null && fileContentTypes.containsKey(filename)) return fileContentTypes.get(filename);
		
		File file = new File(filename);
		String contentType = defaultType;
		try {
			contentType = Files.probeContentType(file.toPath());
		} catch (IOException e) {
			log.info("Failed to determine content type of attachment ["+filename+"]: ", e);
		}
		return contentType;
	}
	
	public void setAttachmentContentType(String filename, String contentType) {
		fileContentTypes.put(filename, contentType);
	}
	
}
