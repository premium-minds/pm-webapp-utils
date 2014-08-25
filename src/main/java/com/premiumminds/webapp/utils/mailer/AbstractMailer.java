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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMailer {

	/**
	 * Generic mailer configurations
	 */
	protected static class Configuration {
		public static final String NAMESPACE = "mail";

		public static class SMTP {
			public static final String NAMESPACE = Configuration.NAMESPACE + ".smtp";
			public static final String HOST = NAMESPACE + ".auth";
			public static final String USER = NAMESPACE + ".user";
			public static final String PASSWORD = NAMESPACE + ".password";
			public static final String AUTH = NAMESPACE + ".auth";

			/**needed for SendGrid*/
			public static final String FROM = NAMESPACE + ".from";
		}

		public static class Debug {
			public static final String NAMESPACE = Configuration.NAMESPACE + ".debug";
			public static final String DEBUG_ADDRESS = NAMESPACE + ".to";
		}
	}

	//Email validation regex
	private static final Pattern EMAIL_PATTERN = 
			Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");


	private static final Logger log = LoggerFactory.getLogger(AbstractMailer.class);
	private static Properties mailConfig;

	/**
	 * Gets the configuration property for the given key
	 * @param key The configuration property key
	 * @return The configuration property value
	 */
	protected static String getConfigurationProperty(String key) {
		return mailConfig.getProperty(key);
	}

	/**
	 * Gets the configuration property for the given key
	 * @param key The configuration property key
	 * @param defaultValue The default value to be returned if the actual value is null
	 * @return The configuration property value
	 */
	protected static String getConfigurationProperty(String key, String defaultValue) {
		String value = mailConfig.getProperty(key);
		return value == null ? defaultValue : value;
	}

	/**
	 * Gets the Properties object with the mailer configurations.
	 * @return The mailer properties.
	 */
	protected static Properties getConfiguration() {
		return mailConfig;
	}

	/**
	 * Loads mailer properties from a given configuration file input stream
	 * @param input The properties file input stream
	 * @throws IOException
	 */
	public final static void configure(InputStream input) throws IOException{
		Properties properties = new Properties();
		properties.load(input);
		configure(properties);
	}

	public final static void configure(Properties properties) throws IOException{
		if(isValidBasicConfiguration(properties)) {
			mailConfig = properties;
		} else {
			log.error("The mailer basic configuration is not valid");
		}
	}

	/**
	 * Returns whether or not the mailer is in debug mode
	 * @return
	 */
	public final boolean isDebug() {
		String debug = getConfigurationProperty(Configuration.Debug.NAMESPACE);
		return new String("true").equalsIgnoreCase(debug);
	}

	/**
	 * Returns the debug mode email address.
	 * All emails will be sent to this address when in debug mode.
	 * @return A string with the debug email address
	 */
	public final String getDebugAddress() {
		String debugAddress = getConfigurationProperty(Configuration.Debug.DEBUG_ADDRESS);
		return debugAddress;
	}

	/**
	 * Converts a given string representation of an email address to an internet address
	 * @param mail The string email address
	 * @return The internet address for the email address given in the input
	 * @throws MailerException
	 */
	protected static InternetAddress convertStringToAddress(String mail) throws MailerException {
		try {
			return new InternetAddress(mail);
		} catch (AddressException e) {
			throw new MailerException(e);
		}
	}

	/**
	 * Converts a list of strings to a list of corresponding internet addresses
	 * @param mails The list of string addresses
	 * @return A list of internet addresses
	 * @throws MailerException
	 */
	protected static Collection<InternetAddress> convertStringsToAddressess(Collection<String> mails) throws MailerException{
		List<InternetAddress> to = new ArrayList<InternetAddress>();
		if(mails!=null){
			for(String mail : mails){
				to.add(convertStringToAddress(mail));
			}
		}
		return to;
	}

	/**
	 * Helper function to get a list from a configuration property value
	 * @param value the property value
	 * @return a list of String property values
	 */
	public static List<String> getList(String value){
		String[] values;

		if(value.isEmpty()){
			values = new String[0];
		} else {
			values = value.split(";");
		}

		return Arrays.asList(values);
	}	

	/**
	 * Checks if the mailer configurations are all correct
	 * @param config The configuration properties
	 * @return true if OK
	 */
	protected final boolean isValidConfiguration(Properties config) {
		return isValidBasicConfiguration(config)
				&& isValidMailerConfiguration(config);
	}

	/**
	 * Checks if any essential configurations are incorrect
	 * @param config The configuration properties
	 * @return true if OK
	 */
	protected static final boolean isValidBasicConfiguration(Properties config) {
		return config != null
				&& config.getProperty(Configuration.SMTP.HOST) != null
				&& config.getProperty(Configuration.SMTP.USER) != null
				&& config.getProperty(Configuration.SMTP.PASSWORD) != null;
	}

	/**
	 * Helper function to aid in the validation of email addresses.
	 * @param email The email address to be validated.
	 * @return true if the email address is valid.
	 */
	public static final boolean isValidEmail(String email) {
		return EMAIL_PATTERN.matcher(email).matches();
	}


	// Abstract methods

	/**
	 * Checks if any implementation specific mailer configurations are incorrect
	 * @param config the configuration properties
	 * @return true if OK
	 */
	protected abstract boolean isValidMailerConfiguration(Properties config);

	/**
	 * Sends an email
	 * @param message The Mime message object to be sent
	 */
	public abstract void send(MimeMessage message) throws MailerException;


	// Single recipient

	/**
	 * Sends an email
	 * @param to The destination email
	 * @param subject the message subject
	 * @param body The message body
	 * @throws MailerException
	 */
	public abstract void send(String to, String subject, String body) throws MailerException;

	/**
	 * Sends an email
	 * @param to The destination email
	 * @param headers A map with any header values to be added or overridden
	 * @param subject the message subject
	 * @param body The message body
	 * @throws MailerException
	 */
	public abstract void send(String to, Map<String, String> headers, String subject, String body) throws MailerException;


	//Several recipients

	/**
	 * Sends an email
	 * @param to The destination email
	 * @param ccs Carbon copy addresses
	 * @param bccs Carbon copy addresses
	 * @param subject the message subject
	 * @param body The message body
	 * @throws MailerException
	 */
	public abstract void send(Collection<String> to, Collection<String> ccs, Collection<String> bccs, String subject, String body) throws MailerException;

	/**
	 * Sends an email
	 * @param to The destination email
	 * @param ccs Carbon copy addresses
	 * @param bccs Carbon copy addresses
	 * @param headers A map with any header values to be added or overridden
	 * @param subject the message subject
	 * @param body The message body
	 * @throws MailerException
	 */
	public abstract void send(Collection<String> to, Collection<String> ccs, Collection<String> bccs, Map<String, String> headers, String subject, String body) throws MailerException;

}
