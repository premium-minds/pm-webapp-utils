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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.premiumminds.webapp.utils.WebAppFileLoader;

/**
 * Class to provide configurations for the Mailer class
 * this uses the context parameter "mailer-config"
 * 
 * This uses the WebAppFileLoader
 * 
 * @author acamilo
 *
 */
public class ContextMailerListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(ContextMailerListener.class);

	@SuppressWarnings("deprecation")
	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );

	    String url = context.getInitParameter("mailer-config");
	    
		if(url==null) throw new RuntimeException("couldn't get webapp parameter 'mailer-config'");
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));

	    try {
			Mailer.configure(fileLoader.load());
			AbstractMailer.configure(fileLoader.load());
		} catch (Exception e) {
			log.error("couldn't load the Mailer properties on '"+url+"'", e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
