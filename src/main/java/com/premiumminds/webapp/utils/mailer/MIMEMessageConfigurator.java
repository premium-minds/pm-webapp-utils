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
