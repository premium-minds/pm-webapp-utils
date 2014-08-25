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
package com.premiumminds.webapp.utils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class to load the Application configurations at the servlet context
 * the context parameter used is "app-config"
 * 
 * @author acamilo
 *
 */
public class ApplicationContextConfigurationListener implements ServletContextListener {
	private static final Logger log = LoggerFactory.getLogger(ApplicationContextConfigurationListener.class);

	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );

	    String url = context.getInitParameter("app-config");
	    
		if(url==null) throw new RuntimeException("couldn't get webapp parameter 'app-config'");
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));
	    
	    try {
			ApplicationContextConfiguration.configure(fileLoader.load());
		} catch (Exception e) {
			log.error("couldn't load the properties file '"+url+"'");
			log.error("error loading application properties", e);
		}
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
