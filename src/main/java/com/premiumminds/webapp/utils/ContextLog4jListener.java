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

import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Class to load Log4j configurations at the servlet context
 * the context parameter "logger-config" is used
 * 
 * @author acamilo
 *
 */
public class ContextLog4jListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent sce) {
	    ServletContext context = sce.getServletContext( );
	    
	    String url = context.getInitParameter("logger-config");
	    
	    WebAppFileLoader fileLoader = new WebAppFileLoader(url, sce.getServletContext( ));

	    try {
			Configurator.initialize(null, new ConfigurationSource(fileLoader.load()));
		} catch (Exception e) {
			System.err.println("Couldn't load the log4j properties file on '"+url+"'");
			e.printStackTrace();
		}
	    
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

}
