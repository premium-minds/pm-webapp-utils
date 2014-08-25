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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

@Deprecated
public class Mailer {
	static Properties mailConfig;
	
	public Mailer(){
	}

	public static void sendMail(String to, String subject, String body) throws MailerException{
		List<InternetAddress> tos = new ArrayList<InternetAddress>();
		try {
			tos.add(new InternetAddress(to));
		} catch (AddressException e) {
			throw new MailerException(e);
		}
		sendMail(tos, null, subject, body);
	}

	public static void sendMail(String to, String cc, String subject, String body) throws MailerException{
		try {
			List<InternetAddress> tos = new ArrayList<InternetAddress>();
			tos.add(new InternetAddress(to));
			List<InternetAddress> ccs = new ArrayList<InternetAddress>();
			ccs.add(new InternetAddress(cc));
			
			sendMail(tos, ccs, subject, body);
		} catch (AddressException e) {
			throw new MailerException(e);
		}
	}

	public static void sendMail(List<InternetAddress> to, String subject, String body) throws MailerException{
		sendMail(to, null, subject, body);
	}
	
	public static void sendMail(List<InternetAddress> to, List<InternetAddress> cc, String subject, String body) throws MailerException{
		if(mailConfig==null) throw new MailerException("Mailer was not configured");
		Session session = Session.getDefaultInstance(mailConfig);
		
		MimeMessage message = new MimeMessage(session);
		
		try {
			if(mailConfig.containsKey("mail.debug.to")){
				for(InternetAddress address : convertStrintToAddress(getList(mailConfig.getProperty("mail.debug.to")))){
					message.addRecipient(RecipientType.TO, address);
				}
			} else {
				for(InternetAddress address : to){
					message.addRecipient(RecipientType.TO, address);
				}
				if(cc!=null){
					for(InternetAddress address : cc){
						message.addRecipient(RecipientType.CC, address);
					}
				}
			}
			message.setSubject(subject);
			message.setText(body);
			message.saveChanges();
			sendMail(message, session);
		} catch (AddressException e) {
			throw new MailerException(e);
		} catch (MessagingException e) {
			throw new MailerException(e);
		}
	}
	
	public static List<InternetAddress> convertStrintToAddress(List<String> mails) throws MailerException{
		List<InternetAddress> to = new ArrayList<InternetAddress>();
		if(mails!=null){
			for(String mail : mails){
				try {
					to.add(new InternetAddress(mail));
				} catch (AddressException e) {
					throw new MailerException(e);
				}
			}
		}
		return to;
	}
	
	private static void sendMail(MimeMessage message, Session session) throws MailerException{
		try {
			Transport tr = session.getTransport("smtp");
			if(mailConfig.getProperty("mail.smtp.auth", "true").equalsIgnoreCase("true")){
				tr.connect(mailConfig.getProperty("mail.smtp.user"), mailConfig.getProperty("mail.smtp.password"));
			} else {
				tr.connect();
			}
			tr.sendMessage(message, message.getAllRecipients());
			tr.close();
		} catch (MessagingException e) {
			throw new MailerException(e);
		}
	}
	
	public static void configure(InputStream input) throws IOException{
		Properties properties = new Properties();
		properties.load(input);
		
		mailConfig = properties;
	}
	
	public static List<String> getList(String value){
		String[] values;
		
		if(value.isEmpty()){
			values = new String[0];
		} else {
			values = value.split(";");
		}
		
		return Arrays.asList(values);
	}	
}
